(function () {
    const quantityContainer = document.querySelector(".quantity");
    const minusBtn = quantityContainer.querySelector(".minus");
    const plusBtn = quantityContainer.querySelector(".plus");
    const inputBox = quantityContainer.querySelector(".input-box");

    updateButtonStates();

    quantityContainer.addEventListener("click", handleButtonClick);
    inputBox.addEventListener("input", handleQuantityChange);

    function updateButtonStates() {
        const value = parseInt(inputBox.value);
        minusBtn.disabled = value <= 1;
        plusBtn.disabled = value >= parseInt(inputBox.max);
    }

    function handleButtonClick(event) {
        if (event.target.classList.contains("minus")) {
            decreaseValue();
        } else if (event.target.classList.contains("plus")) {
            increaseValue();
        }
    }

    function decreaseValue() {
        let value = parseInt(inputBox.value);
        value = isNaN(value) ? 1 : Math.max(value - 1, 1);
        inputBox.value = value;
        updateButtonStates();
        handleQuantityChange();
    }

    function increaseValue() {
        let value = parseInt(inputBox.value);
        value = isNaN(value) ? 1 : Math.min(value + 1, parseInt(inputBox.max));
        inputBox.value = value;
        updateButtonStates();
        handleQuantityChange();
    }

    function handleQuantityChange() {
        let value = parseInt(inputBox.value);
        value = isNaN(value) ? 1 : value;

        // Execute your code here based on the updated quantity value
        console.log("Quantity changed:", value);
    }
})();


const options = document.querySelectorAll('.menu-option');
const priceInfo = document.getElementById('price-info');

options.forEach(option => {
    option.addEventListener('click', () => {
        // Remove 'active' class from all options
        options.forEach(opt => opt.classList.remove('active'));

        // Add 'active' class to the clicked option
        option.classList.add('active');

        // Update price info based on the selected option
        const selectedSize = option.getAttribute('data-size');
        const selectedPrice = option.getAttribute('data-price');
        priceInfo.innerHTML = `${selectedPrice} / ${selectedSize} мл., вкл. ДДС | Код: NAR01085`;
    });
});

document.addEventListener("DOMContentLoaded", function () {
    const tabs = document.querySelectorAll(".tab");
    const contents = document.querySelectorAll(".tab-content");

    // Ensure only the first content is visible on load
    contents.forEach((content, index) => {
        content.style.display = index === 0 ? "block" : "none";
    });

    tabs.forEach((tab, index) => {
        tab.addEventListener("click", function () {
            tabs.forEach(t => t.classList.remove("active"));
            tab.classList.add("active");

            contents.forEach(content => content.style.display = "none");
            contents[index].style.display = "block";
        });
    });
});