document.addEventListener("DOMContentLoaded", function () {
    document.querySelectorAll(".wishlist-btn").forEach(button => {
        button.addEventListener("click", function () {
            this.classList.toggle("active");
        });
    });
});

function addToWishlist(button) {
    const productId = button.getAttribute("data-id");

    fetch(`/wishlist/add/${productId}`, {
        method: "POST",
        headers: {
            "Content-Type": "application/json",
        },
    })
        .then(response => {
            if (response.ok) {
                alert("Продуктът беше добавен в списъка с желания!");
            } else {
                alert("Грешка при добавяне в списъка с желания.");
            }
        })
        .catch(error => console.error("Error:", error));
}