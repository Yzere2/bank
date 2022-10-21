function sendJSON(){

    let result = document.querySelector('#result1');
    let name = document.querySelector('#name');
    let password = document.querySelector('#password');
    let execute = document.querySelector('#execute');
    let amount = document.querySelector('#amount');
    let blik = document.querySelector('#blik');
    parseInt(execute);
    parseFloat(amount);

    // Creating a XHR object
    let xhr = new XMLHttpRequest();
    let url = "http://localhost:8080/api/transaction/auth";

    // open a connection
    xhr.open("POST", url, true);

    // Set the request header i.e. which type of content you are sending
    xhr.setRequestHeader("Content-Type", "application/json");

    // Create a state change callback
    xhr.onreadystatechange = function () {
        if (xhr.readyState === 4 && xhr.status === 200) {

            // Print received data from server
            result.innerHTML = this.responseText;

        }
    };

    // Converting JSON data to string
    var data = JSON.stringify({ "Name": name.value, "Password":password.value,"Execute":execute.value, "Ammount":amount, "BlikCode":blik.value});

    // Sending data with the request
    xhr.send(data);
}