/* Parking kazne - jednostavan frontend nad REST API-jem Spring Boot aplikacije.
   Statika se servira iz classpath:/static, pa je isti origin - nema CORS-a. */

const API = "/api";

/* ------------------------------------------------------------------ */
/* HTTP helper                                                         */
/* ------------------------------------------------------------------ */

/** Salje zahtev i vraca telo odgovora (JSON ako je moguce, inace tekst).
 *  Kontroleri na gresku vracaju 400 sa porukom kao cist tekst. */
async function api(path, options = {}) {
    const res = await fetch(API + path, {
        headers: options.body ? { "Content-Type": "application/json" } : {},
        ...options
    });

    const raw = await res.text();
    let body = raw;
    try {
        body = raw ? JSON.parse(raw) : null;
    } catch (e) {
        /* backend je vratio cist tekst (poruka o gresci ili potvrda) */
    }

    if (!res.ok) {
        throw new Error(typeof body === "string" && body ? body : `Greška ${res.status}`);
    }
    return body;
}

const get = (p) => api(p);
const post = (p, data) => api(p, { method: "POST", body: JSON.stringify(data) });
const del = (p) => api(p, { method: "DELETE" });
const patch = (p) => api(p, { method: "PATCH" });

/** Endpoint-i za listanje ponekad vrate string umesto niza (npr. "No tickets..."). */
const asList = (v) => (Array.isArray(v) ? v : []);

/* ------------------------------------------------------------------ */
/* UI helpers                                                          */
/* ------------------------------------------------------------------ */

const $ = (sel) => document.querySelector(sel);

function toast(message, type = "ok") {
    const el = document.createElement("div");
    el.className = `toast ${type}`;
    el.textContent = message;
    $("#toasts").appendChild(el);
    setTimeout(() => el.remove(), 4000);
}

/** Prikazuje poruku na sredini ekrana (npr. rezultat izdavanja kazne). */
function showModal(message, type = "ok") {
    const overlay = $("#modalOverlay");
    const box = $("#modalBox");
    box.className = `modal-box ${type}`;
    $("#modalIcon").textContent = type === "ok" ? "✓" : "✕";
    $("#modalMessage").textContent = message;
    overlay.hidden = false;
}

function hideModal() {
    $("#modalOverlay").hidden = true;
}

document.addEventListener("DOMContentLoaded", () => {
    $("#modalClose").addEventListener("click", hideModal);
    $("#modalOverlay").addEventListener("click", (e) => {
        if (e.target.id === "modalOverlay") hideModal();
    });
});

function esc(value) {
    if (value === null || value === undefined) return "";
    return String(value).replace(/[&<>"']/g, (c) => ({
        "&": "&amp;", "<": "&lt;", ">": "&gt;", '"': "&quot;", "'": "&#39;"
    }[c]));
}

const dinar = (n) => Number(n || 0).toLocaleString("sr-RS") + " RSD";
const badge = (v) => `<span class="badge ${esc(v)}">${esc(String(v).replace("_", " "))}</span>`;

/** Crta tabelu: columns = [{ head, cell(row) }]. */
function renderTable(containerSel, rows, columns, emptyText = "Nema podataka.") {
    const box = $(containerSel);
    if (!rows.length) {
        box.innerHTML = `<p class="empty">${esc(emptyText)}</p>`;
        return;
    }
    const head = columns.map((c) => `<th>${esc(c.head)}</th>`).join("");
    const body = rows
        .map((r) => `<tr>${columns.map((c) => `<td class="${c.cls || ""}">${c.cell(r)}</td>`).join("")}</tr>`)
        .join("");
    box.innerHTML = `<div class="table-wrap"><table><thead><tr>${head}</tr></thead><tbody>${body}</tbody></table></div>`;
}

/** Vezuje submit forme: skuplja polja, salje, osvezava. */
function bindForm(formSel, handler) {
    $(formSel).addEventListener("submit", async (e) => {
        e.preventDefault();
        const form = e.target;
        const data = Object.fromEntries(new FormData(form).entries());
        try {
            await handler(data, form);
        } catch (err) {
            toast(err.message, "err");
        }
    });
}

/* ------------------------------------------------------------------ */
/* Kes za padajuce liste                                               */
/* ------------------------------------------------------------------ */

const cache = { vozaci: [], vozila: [], zone: [], lokacije: [], kontrolori: [], kazne: [] };

/** Puni sve <select data-src="..."> elemente, cuvajuci trenutni izbor. */
function fillSelects() {
    const sources = {
        "vozaci": cache.vozaci.map((v) => [v.id, `${v.ime} ${v.prezime} (${v.brojVozacke})`]),
        "vozila-reg": cache.vozila.map((v) => [v.registracija, `${v.registracija} — ${v.marka} ${v.model}`]),
        "zone": cache.zone.map((z) => [z.id, `${z.naziv} (${z.zona}, ${z.cenaPoSatu} RSD/h)`]),
        "lokacije": cache.lokacije.map((l) => [l.id, `${l.ulica}, ${l.grad}`]),
        "kontrolori": cache.kontrolori.map((k) => [k.id, `${k.ime} ${k.prezime} (${k.brojLegitimacije})`])
    };

    document.querySelectorAll("select[data-src]").forEach((sel) => {
        const items = sources[sel.dataset.src] || [];
        const previous = sel.value;
        sel.innerHTML = items.length
            ? items.map(([v, t]) => `<option value="${esc(v)}">${esc(t)}</option>`).join("")
            : `<option value="">— prvo unesi podatke —</option>`;
        if (previous && items.some(([v]) => String(v) === previous)) sel.value = previous;
    });
}

/** Padajuca lista neplacenih kazni na tabu Placanja. */
function fillNeplaceneKazne() {
    const sel = $("#selectNeplaceneKazne");
    const sve = cache.kazne;
    sel.innerHTML = sve.length
        ? sve
            .map((k) => `<option value="${k.id}" data-iznos="${k.iznos}">#${k.id} — ${esc(k.registracije)} — ${dinar(k.iznos)} — ${esc(k.status)}</option>`)
            .join("")
        : `<option value="">— nema kazni —</option>`;
    syncIznos();
}

/** Predlaze tacan iznos izabrane kazne. */
function syncIznos() {
    const opt = $("#selectNeplaceneKazne").selectedOptions[0];
    $("#inputIznos").value = opt && opt.dataset.iznos ? opt.dataset.iznos : "";
}

/* ------------------------------------------------------------------ */
/* Ucitavanje i prikaz                                                 */
/* ------------------------------------------------------------------ */

async function loadVozaci() {
    cache.vozaci = asList(await get("/vozac"));
    renderTable("#tabelaVozaci", cache.vozaci, [
        { head: "ID", cell: (v) => v.id },
        { head: "Ime", cell: (v) => esc(v.ime) },
        { head: "Prezime", cell: (v) => esc(v.prezime) },
        { head: "Broj vozačke", cell: (v) => esc(v.brojVozacke) },
        { head: "Telefon", cell: (v) => esc(v.telefon) },
        { head: "", cls: "actions", cell: (v) => delBtn("/vozac/" + v.id, "vozača " + v.ime) }
    ], "Nema unetih vozača.");
}

async function loadVozila() {
    cache.vozila = asList(await get("/vozilo"));
    renderTable("#tabelaVozila", cache.vozila, [
        { head: "ID", cell: (v) => v.id },
        { head: "Registracija", cell: (v) => esc(v.registracija) },
        { head: "Marka", cell: (v) => esc(v.marka) },
        { head: "Model", cell: (v) => esc(v.model) },
        { head: "Vozač", cell: (v) => esc(imeVozaca(v.vozacId)) },
        { head: "", cls: "actions", cell: (v) => delBtn("/vozilo/" + v.id, "vozilo " + v.registracija) }
    ], "Nema unetih vozila.");
}

function imeVozaca(id) {
    const v = cache.vozaci.find((x) => x.id === id);
    return v ? `${v.ime} ${v.prezime}` : "#" + id;
}

async function loadZone() {
    cache.zone = asList(await get("/parkingZona"));
    renderTable("#tabelaZone", cache.zone, [
        { head: "ID", cell: (z) => z.id },
        { head: "Naziv", cell: (z) => esc(z.naziv) },
        { head: "Zona", cell: (z) => badge(z.zona) },
        { head: "Cena po satu", cell: (z) => dinar(z.cenaPoSatu) },
        { head: "", cls: "actions", cell: (z) => delBtn("/parkingZona/" + z.id, "zonu " + z.naziv) }
    ], "Nema unetih parking zona.");
}

async function loadLokacije() {
    cache.lokacije = asList(await get("/lokacije"));
    renderTable("#tabelaLokacije", cache.lokacije, [
        { head: "ID", cell: (l) => l.id },
        { head: "Ulica", cell: (l) => esc(l.ulica) },
        { head: "Grad", cell: (l) => esc(l.grad) },
        { head: "", cls: "actions", cell: (l) => delBtn("/lokacije/" + l.id, "lokaciju " + l.ulica) }
    ], "Nema unetih lokacija.");
}

async function loadKontrolori() {
    cache.kontrolori = asList(await get("/kontrolor"));
    renderTable("#tabelaKontrolori", cache.kontrolori, [
        { head: "ID", cell: (k) => k.id },
        { head: "Ime", cell: (k) => esc(k.ime) },
        { head: "Prezime", cell: (k) => esc(k.prezime) },
        { head: "Broj legitimacije", cell: (k) => esc(k.brojLegitimacije) },
        { head: "", cls: "actions", cell: (k) => delBtn("/kontrolor/" + k.id, "kontrolora " + k.ime) }
    ], "Nema unetih kontrolora.");
}

async function loadKazne() {
    cache.kazne = asList(await get("/kazna"));
    prikaziKazne();
}

/** Prikazuje kazne uz primenjen filter statusa (filtriranje je lokalno). */
function prikaziKazne() {
    const filter = $("#filterStatus").value;
    const rows = filter ? cache.kazne.filter((k) => k.status === filter) : cache.kazne;

    renderTable("#tabelaKazne", rows, [
        { head: "ID", cell: (k) => k.id },
        { head: "Registracija", cell: (k) => esc(k.registracije) },
        { head: "Iznos", cell: (k) => dinar(k.iznos) },
        { head: "Status", cell: (k) => badge(k.status) },
        { head: "Zona", cell: (k) => esc(k.parkingZona) },
        { head: "Ulica", cell: (k) => esc(k.ulica) },
        { head: "Kontrolor", cell: (k) => esc(k.imeKontrolera) },
        { head: "Izdata", cell: (k) => esc(k.vremeIzdavanja) },
        {
            head: "Akcije", cls: "actions", cell: (k) => `
                ${k.status !== "PLACENA"
                    ? `<button class="btn btn-sm" data-status="U_POSTUPKU" data-id="${k.id}">U postupku</button>` : ""}
                ${k.status !== "NEPLACENA"
                    ? `<button class="btn btn-sm" data-status="NEPLACENA" data-id="${k.id}">Neplaćena</button>` : ""}
                ${delBtn("/kazna/" + k.id, "kaznu #" + k.id)}`
        }
    ], "Nema kazni za prikaz.");
}

async function loadKarte() {
    const karte = asList(await get("/dnevnaKarta"));
    const sada = new Date();
    renderTable("#tabelaKarte", karte, [
        { head: "ID", cell: (k) => k.id },
        { head: "Registracija", cell: (k) => esc(k.registracija) },
        { head: "Sati", cell: (k) => k.brojSati },
        { head: "Cena", cell: (k) => dinar(k.cena) },
        { head: "Kupljena", cell: (k) => esc(k.vremeKupovine) },
        { head: "Važi do", cell: (k) => esc(k.vaziDo) },
        {
            head: "Status", cell: (k) => {
                const vazi = k.vaziDo && new Date(k.vaziDo.replace(" ", "T")) > sada;
                return `<span class="badge ${vazi ? "aktivna" : "istekla"}">${vazi ? "aktivna" : "istekla"}</span>`;
            }
        },
        { head: "", cls: "actions", cell: (k) => delBtn("/dnevnaKarta/" + k.id, "kartu #" + k.id) }
    ], "Nema kupljenih dnevnih karata.");
}

async function loadPlacanja() {
    const placanja = asList(await get("/placanje"));
    renderTable("#tabelaPlacanja", placanja, [
        { head: "ID", cell: (p) => p.id },
        { head: "Kazna", cell: (p) => "#" + p.kaznaId },
        { head: "Iznos", cell: (p) => dinar(p.iznos) },
        { head: "Datum", cell: (p) => esc(p.datum) },
        { head: "Način", cell: (p) => esc(p.nacin) },
        { head: "Status kazne", cell: (p) => badge(p.status) }
    ], "Nema evidentiranih plaćanja.");
    return placanja;
}

/** Kartice sa zbirnim brojkama na tabu Pregled. */
function renderStats(placanja) {
    const neplacene = cache.kazne.filter((k) => k.status === "NEPLACENA");
    const dug = neplacene.reduce((s, k) => s + k.iznos, 0);
    const naplaceno = placanja.reduce((s, p) => s + p.iznos, 0);

    const items = [
        { label: "Ukupno kazni", value: cache.kazne.length },
        { label: "Neplaćene kazne", value: neplacene.length, cls: "red" },
        { label: "Dug po neplaćenim", value: dinar(dug), cls: "red" },
        { label: "Naplaćeno", value: dinar(naplaceno), cls: "green" },
        { label: "Vozila", value: cache.vozila.length },
        { label: "Vozači", value: cache.vozaci.length },
        { label: "Parking zone", value: cache.zone.length },
        { label: "Kontrolori", value: cache.kontrolori.length }
    ];

    $("#stats").innerHTML = items
        .map((i) => `<div class="stat"><div class="label">${esc(i.label)}</div>
                     <div class="value ${i.cls || ""}">${esc(i.value)}</div></div>`)
        .join("");
}

const delBtn = (path, opis) =>
    `<button class="btn btn-sm danger" data-del="${esc(path)}" data-opis="${esc(opis)}">Obriši</button>`;

/** Ucitava sve sekcije. Sifrarnici prvo, jer kazne i vozila prikazuju njihove nazive. */
async function refreshAll() {
    try {
        await Promise.all([loadVozaci(), loadZone(), loadLokacije(), loadKontrolori()]);
        await Promise.all([loadVozila(), loadKazne(), loadKarte()]);
        const placanja = await loadPlacanja();
        fillSelects();
        fillNeplaceneKazne();
        renderStats(placanja);
    } catch (err) {
        toast("Greška pri učitavanju: " + err.message, "err");
    }
}

/* ------------------------------------------------------------------ */
/* Dogadjaji                                                           */
/* ------------------------------------------------------------------ */

// tabovi
$("#tabs").addEventListener("click", (e) => {
    const tab = e.target.closest(".tab");
    if (!tab) return;
    document.querySelectorAll(".tab").forEach((t) => t.classList.toggle("active", t === tab));
    document.querySelectorAll(".panel").forEach((p) =>
        p.classList.toggle("active", p.id === "tab-" + tab.dataset.tab));
});

$("#refreshAll").addEventListener("click", refreshAll);
$("#filterStatus").addEventListener("change", prikaziKazne);
$("#selectNeplaceneKazne").addEventListener("change", syncIznos);

// brisanje i promena statusa - delegirano, jer se dugmad iscrtavaju dinamicki
document.addEventListener("click", async (e) => {
    const btn = e.target.closest("button");
    if (!btn) return;

    if (btn.dataset.del) {
        if (!confirm(`Obrisati ${btn.dataset.opis}?`)) return;
        try {
            await del(btn.dataset.del);
            toast("Obrisano.");
            await refreshAll();
        } catch (err) {
            toast(err.message, "err");
        }
        return;
    }

    if (btn.dataset.status) {
        try {
            await patch(`/kazna/${btn.dataset.id}/status?statusKazne=${btn.dataset.status}`);
            toast("Status kazne je promenjen.");
            await refreshAll();
        } catch (err) {
            toast(err.message, "err");
        }
    }
});

/* --- forme --- */

bindForm("#formVozac", async (d, form) => {
    await post("/vozac", d);
    toast("Vozač je sačuvan.");
    form.reset();
    await refreshAll();
});

bindForm("#formVozilo", async (d, form) => {
    await post("/vozilo", { ...d, vozacId: Number(d.vozacId) });
    toast("Vozilo je sačuvano.");
    form.reset();
    await refreshAll();
});

bindForm("#formZona", async (d, form) => {
    await post("/parkingZona", { ...d, cenaPoSatu: Number(d.cenaPoSatu) });
    toast("Parking zona je sačuvana.");
    form.reset();
    await refreshAll();
});

bindForm("#formLokacija", async (d, form) => {
    await post("/lokacije", d);
    toast("Lokacija je sačuvana.");
    form.reset();
    await refreshAll();
});

bindForm("#formKontrolor", async (d, form) => {
    await post("/kontrolor", d);
    toast("Kontrolor je sačuvan.");
    form.reset();
    await refreshAll();
});

bindForm("#formKazna", async (d) => {
    let kazna;
    try {
        kazna = await post("/kazna", {
            registracija: d.registracija,
            parkingZonaId: Number(d.parkingZonaId),
            kontrolerId: Number(d.kontrolerId),
            lokacijaId: Number(d.lokacijaId)
        });
    } catch (err) {
        showModal(err.message, "err");
        return;
    }
    showModal(`Kazna #${kazna.id} uspešno izdata — ${dinar(kazna.iznos)}.`, "ok");
    await refreshAll();
});

bindForm("#formKarta", async (d) => {
    const karta = await post("/dnevnaKarta", {
        brojSati: Number(d.brojSati),
        registracija: d.registracija,
        parkingZonaId: Number(d.parkingZonaId)
    });
    toast(`Karta kupljena — ${dinar(karta.cena)}, važi do ${karta.vaziDo}.`);
    await refreshAll();
});

bindForm("#formPlacanje", async (d) => {
    if (!d.kaznaId) {
        showModal("Nema izabrane kazne.", "err");
        return;
    }
    try {
        await post("/placanje", {
            kaznaId: Number(d.kaznaId),
            iznos: Number(d.iznos),
            nacin: d.nacin
        });
    } catch (err) {
        showModal(err.message, "err");
        return;
    }
    showModal("Plaćanje je uspešno evidentirano, kazna je plaćena.", "ok");
    await refreshAll();
});

// provera vozila po registraciji
bindForm("#formProvera", async (d) => {
    const reg = d.registracija.trim();
    const [kazne, karte] = await Promise.all([
        get("/kazna/vozilo/" + encodeURIComponent(reg)),
        get("/dnevnaKarta/registracija/" + encodeURIComponent(reg))
    ]);

    const listaKazni = asList(kazne);
    const listaKarata = asList(karte);
    const dug = listaKazni.filter((k) => k.status === "NEPLACENA").reduce((s, k) => s + k.iznos, 0);

    const red = (cells) => `<tr>${cells.map((c) => `<td>${c}</td>`).join("")}</tr>`;

    $("#proveraRezultat").innerHTML = `
        <div class="result-block">
            <h3>Kazne za ${esc(reg)} — neplaćeno: ${dinar(dug)}</h3>
            ${listaKazni.length
                ? `<div class="table-wrap"><table>
                     <thead><tr><th>ID</th><th>Iznos</th><th>Status</th><th>Zona</th><th>Izdata</th></tr></thead>
                     <tbody>${listaKazni.map((k) => red([k.id, dinar(k.iznos), badge(k.status),
                                 esc(k.parkingZona), esc(k.vremeIzdavanja)])).join("")}</tbody>
                   </table></div>`
                : `<p class="empty">Nema kazni za ovo vozilo.</p>`}
        </div>
        <div class="result-block">
            <h3>Dnevne karte</h3>
            ${listaKarata.length
                ? `<div class="table-wrap"><table>
                     <thead><tr><th>ID</th><th>Sati</th><th>Cena</th><th>Važi do</th></tr></thead>
                     <tbody>${listaKarata.map((k) => red([k.id, k.brojSati, dinar(k.cena),
                                 esc(k.vaziDo)])).join("")}</tbody>
                   </table></div>`
                : `<p class="empty">Nema dnevnih karata za ovo vozilo.</p>`}
        </div>`;
});

refreshAll();
