function checkSelectedGoods(e, orderId) {
    var checkboxes = document.querySelectorAll(".order" + orderId);
    var selectedCount = 0;
    checkboxes.forEach(function(checkbox) {
        if (checkbox.checked) selectedCount++;
    });
    if (selectedCount === 0) {
        e.preventDefault();
        alert("Вы не выбрали ни одного товара в заказе под номером №" + orderId);
    }
}