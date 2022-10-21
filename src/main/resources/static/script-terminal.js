function sendJSON(){

    let result = document.querySelector('#result');
    let name = document.querySelector('#name');
    let password = document.querySelector('#password');
    let execute = document.querySelector('#execute');
    parseInt(execute);

    // Creating a XHR object
    let xhr = new XMLHttpRequest();
    let url = "http://localhost:8080/api/user/new";

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
    var data = JSON.stringify({ "Name": name.value, "Password":password.value,"Execute":execute.value});

    // Sending data with the request
    xhr.send(data);
}

function sendJSONBlik(){

    let result = document.querySelector('#result2');
    let name = document.querySelector('#name2');
    let password = document.querySelector('#password2');
    let execute = document.querySelector('#execute2');
    parseInt(execute);

    // Creating a XHR object
    let xhr = new XMLHttpRequest();
    let url = "http://localhost:8080/api/user/auth";

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
    var data = JSON.stringify({ "Name": name.value, "Password":password.value,"Execute":execute.value});

    // Sending data with the request
    xhr.send(data);
}