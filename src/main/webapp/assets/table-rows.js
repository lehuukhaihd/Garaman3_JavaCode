document.querySelectorAll(".clickable-row[data-href]").forEach((row) => {
    row.addEventListener("click", (event) => {
        if (event.target.closest("a, button")) {
            return;
        }
        window.location.assign(row.dataset.href);
    });
});
