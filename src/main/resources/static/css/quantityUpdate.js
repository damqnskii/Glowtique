document.addEventListener("DOMContentLoaded", function () {
    document.querySelectorAll(".quantity").forEach((container) => {
        const minusBtn = container.querySelector(".minus");
        const plusBtn = container.querySelector(".plus");
        const quantityInput = container.querySelector(".input-box");

        const productId = quantityInput.closest(".cart-item").getAttribute("data-product-id");

        async function updateQuantity(newQuantity) {
            try {
                const response = await fetch(`/cart/update-quantity/${productId}?quantity=${newQuantity}`, {
                    method: "PUT",
                    headers: {
                        "Content-Type": "application/json"
                    }
                });

                if (response.ok) {
                    const data = await response.json();
                    quantityInput.value = newQuantity;
                    document.querySelector(".total-price").innerHTML = `Крайна цена с ДДС: <strong>${data.totalPrice} лв.</strong>`; // Update total price
                } else {
                    alert("Failed to update quantity.");
                }
            } catch (error) {
                console.error("Error updating cart:", error);
            }
        }

        minusBtn.addEventListener("click", function (e) {
            e.preventDefault();
            let currentQuantity = parseInt(quantityInput.value);
            if (currentQuantity > 1) {
                updateQuantity(currentQuantity - 1);
            }
        });

        plusBtn.addEventListener("click", function (e) {
            e.preventDefault();
            let currentQuantity = parseInt(quantityInput.value);
            if (currentQuantity < 10) {
                updateQuantity(currentQuantity + 1);
            }
        });

        quantityInput.addEventListener("change", function () {
            let newQuantity = parseInt(quantityInput.value);
            if (newQuantity < 1) {
                newQuantity = 1;
            } else if (newQuantity > 10) {
                newQuantity = 10;
            }
            updateQuantity(newQuantity);
        });
    });
});
