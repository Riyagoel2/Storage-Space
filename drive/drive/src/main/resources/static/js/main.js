'use strict';


var singleUploadForm = document.querySelector('#singleUploadForm');
var singleFileUploadInput = document.querySelector('#singleFileUploadInput');
var singleFileUploadError = document.querySelector('#singleFileUploadError');
var singleFileUploadSuccess = document.querySelector('#singleFileUploadSuccess');
function uploadSingleFile(file) {
 
    var formData = new FormData();
       var userid = document.getElementById("userid").value;
 
    formData.append("file", file);

    var xhr = new XMLHttpRequest();
    xhr.open("POST", "/home/uploadFile/"+userid);

    xhr.onload = function() {
        console.log(xhr.responseText);
        var response = JSON.parse(xhr.responseText);
        if(xhr.status === 200) {
            singleFileUploadError.style.display = "none";
            singleFileUploadSuccess.innerHTML = "<p>File Uploaded</p>";
            singleFileUploadSuccess.style.display = "block";
        } else {
            singleFileUploadSuccess.style.display = "none";
            singleFileUploadError.innerHTML =   "Some Error Occurred";
           
        }
    }

    xhr.send(formData);
 }

singleUploadForm.addEventListener('submit', function(event){
    var files = singleFileUploadInput.files;
    uploadSingleFile(files[0]);
     
    
    event.preventDefault();
    document.getElementById('singleUploadForm').reset();
}, true);



 

