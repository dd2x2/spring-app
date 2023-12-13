document.addEventListener("DOMContentLoaded", function() {
    document.getElementById('close').onclick = function() {
        document.getElementById('myModal').style.display = "none";
    }
});
function submitForm(event, orderId) {
    checkSelectedGoods(event, orderId);
    if(!event.defaultPrevented) {
        completeOrder(event, orderId);
    }
}

function completeOrder(event, orderId) {
    event.preventDefault();
    let form = document.querySelector(`form[action='/storekeeper/orders/complete/${orderId}']`);
    let formData = new FormData(form);
    fetch(form.action, {
        method: 'POST',
        body: new URLSearchParams(formData),
    }).then(res => {
        if (res.ok) {
            location.reload();
        } else {
            res.text().then(errorMessage => openModal(errorMessage))
        }
    }).catch(err => {
        openModal(err);
    });
}
function checkSelectedGoods(e, orderId) {
    var checkboxes = document.querySelectorAll(".order" + orderId);
    var selectedCount = 0;
    checkboxes.forEach(function(checkbox) {
        if (checkbox.checked) selectedCount++;
    });
    if (selectedCount === 0) {
        e.preventDefault();
        openModal("Вы не выбрали ни одного товара в заказе под номером №" + orderId);
    }
}

function toggle(source, orderClass) {
    checkboxes = document.querySelectorAll("." + orderClass);
    for (var i = 0, n = checkboxes.length; i < n; i++) {
        checkboxes[i].checked = source.checked;
    }
}

function enableDisableTextBox(id) {
    const checkbox = document.getElementById('checkbox_' + id);
    const textbox = document.getElementById('text_' + id);
    textbox.disabled = !checkbox.checked;
}

function openModal(text) {
    document.getElementById('modal-text').innerText = text;
    document.getElementById('myModal').style.display = "block";
}

document.addEventListener("DOMContentLoaded", function() {
    document.getElementById('close').onclick = function() {
        document.getElementById('myModal').style.display = "none";
    }
});