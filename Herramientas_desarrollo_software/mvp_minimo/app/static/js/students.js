const searchInput = document.querySelector("#student-search");
const levelFilter = document.querySelector("#level-filter");
const rows = [...document.querySelectorAll("[data-student-row]")];
const resultCount = document.querySelector("#result-count");
const filteredEmpty = document.querySelector("#filtered-empty");

function applyFilters() {
  const query = searchInput.value.trim().toLocaleLowerCase("es");
  const level = levelFilter.value;
  let visible = 0;
  rows.forEach((row) => {
    const matches = row.dataset.search.includes(query) && (!level || row.dataset.level === level);
    row.hidden = !matches;
    if (matches) visible += 1;
  });
  resultCount.textContent = `${visible} ${visible === 1 ? "estudiante" : "estudiantes"}`;
  filteredEmpty.hidden = visible !== 0 || rows.length === 0;
}

searchInput?.addEventListener("input", applyFilters);
levelFilter?.addEventListener("change", applyFilters);
