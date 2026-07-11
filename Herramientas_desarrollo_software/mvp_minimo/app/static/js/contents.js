const rows = Array.from(document.querySelectorAll("[data-content-row]"));
const searchInput = document.querySelector("#content-search");
const topicFilter = document.querySelector("#topic-filter");
const levelFilter = document.querySelector("#level-filter");
const resultCount = document.querySelector("#result-count");
const filteredEmpty = document.querySelector("#filtered-empty");

function applyFilters() {
  const term = (searchInput?.value || "").trim().toLowerCase();
  const topic = topicFilter?.value || "";
  const level = levelFilter?.value || "";
  let visible = 0;

  rows.forEach((row) => {
    const matchesTerm = !term || row.dataset.search.includes(term);
    const matchesTopic = !topic || row.dataset.topic === topic;
    const matchesLevel = !level || row.dataset.level === level;
    const shouldShow = matchesTerm && matchesTopic && matchesLevel;
    row.hidden = !shouldShow;
    if (shouldShow) visible += 1;
  });

  if (resultCount) {
    resultCount.textContent = `${visible} ${visible === 1 ? "contenido" : "contenidos"}`;
  }
  if (filteredEmpty) {
    filteredEmpty.hidden = visible > 0 || rows.length === 0;
  }
}

[searchInput, topicFilter, levelFilter].forEach((control) => {
  control?.addEventListener("input", applyFilters);
  control?.addEventListener("change", applyFilters);
});
