function enableDisableTextBox(id) {
    const checkbox = document.getElementById('checkbox_' + id);
    const textbox = document.getElementById('text_' + id);
    textbox.disabled = !checkbox.checked;
}