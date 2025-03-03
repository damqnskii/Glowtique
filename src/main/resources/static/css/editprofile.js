document.getElementById('editBtn').addEventListener('click', function () {
    let inputs = document.querySelectorAll('input, select');
    inputs.forEach(input => input.removeAttribute('disabled'));

    // Enable Save button
    document.getElementById('saveBtn').removeAttribute('disabled');

    // Optional: Disable edit button after clicking
    this.setAttribute('disabled', 'true');
});