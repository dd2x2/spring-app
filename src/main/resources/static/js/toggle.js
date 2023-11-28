function toggle(source, orderClass) {
    checkboxes = document.querySelectorAll("." + orderClass);
    for (var i = 0, n = checkboxes.length; i < n; i++) {
        checkboxes[i].checked = source.checked;
    }
}