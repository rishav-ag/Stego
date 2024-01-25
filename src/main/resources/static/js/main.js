'use strict';

var coverUploadForm = document.querySelector('#coverUploadForm');
var coverFileUploadInput = document.querySelector('#coverFileUploadInput');
var coverFileUploadError = document.querySelector('#coverFileUploadError');
var coverFileUploadSuccess = document.querySelector('#coverFileUploadSuccess');

var secretUploadForm = document.querySelector('#secretUploadForm');
var secretFileUploadInput = document.querySelector('#secretFileUploadInput');
var secretFileUploadError = document.querySelector('#secretFileUploadError');
var secretFileUploadSuccess = document.querySelector('#secretFileUploadSuccess');

var encryptedError = document.querySelector('#encryptedFileDownloadError');
var encryptedSuccess = document.querySelector('#encryptedFileDownloadSuccess');

function uploadCoverFile(file) {
    var formData = new FormData();
    formData.append("file", file);

    var xhr = new XMLHttpRequest();
    xhr.open("POST", "/uploadCoverFile");

    xhr.onload = function() {
        console.log(xhr.responseText);
        var response = JSON.parse(xhr.responseText);
        if(xhr.status == 200) {
            coverFileUploadError.style.display = "none";
            coverFileUploadSuccess.innerHTML = "<p>File Uploaded Successfully.</p><p>DownloadUrl : <a href='" + response.fileDownloadUri + "' target='_blank'>" + response.fileDownloadUri + "</a></p>";
            coverFileUploadSuccess.style.display = "block";
        } else {
            coverFileUploadSuccess.style.display = "none";
            coverFileUploadError.innerHTML = (response && response.message) || "Some Error Occurred";
        }
    }

    xhr.send(formData);
}

function uploadSecretFile(file) {
    var formData = new FormData();
    formData.append("file", file);

    var xhr = new XMLHttpRequest();
    xhr.open("POST", "/uploadSecretFile");

    xhr.onload = function() {
        console.log(xhr.responseText);
        var response = JSON.parse(xhr.responseText);
        if(xhr.status == 200) {
            secretFileUploadError.style.display = "none";
            secretFileUploadSuccess.innerHTML = "<p>File Uploaded Successfully.</p><p>DownloadUrl : <a href='" + response.fileDownloadUri + "' target='_blank'>" + response.fileDownloadUri + "</a></p>";
            secretFileUploadSuccess.style.display = "block";
        } else {
            secretFileUploadSuccess.style.display = "none";
            secretFileUploadError.innerHTML = (response && response.message) || "Some Error Occurred";
        }
    }

    xhr.send(formData);//contentType(MediaType.parseMediaType(contentType))
}

coverUploadForm.addEventListener('submit', function(event){
    var files = coverFileUploadInput.files;
    if(files.length === 0) {
        coverFileUploadError.innerHTML = "Please select a file";
        coverFileUploadError.style.display = "block";
    }
    uploadCoverFile(files[0]);
    event.preventDefault();
}, true);


secretUploadForm.addEventListener('submit', function(event){
    var files = secretFileUploadInput.files;
    if(files.length === 0) {
        secretFileUploadError.innerHTML = "Please select a file";
        secretFileUploadError.style.display = "block";
    }
    uploadSecretFile(files[0]);
    event.preventDefault();
}, true);

function generateImage() {
    var xhr = new XMLHttpRequest();
    xhr.open("GET", "/getEncryptedImage");

    xhr.onload = function() {
        console.log(xhr.responseText);
        var response = JSON.parse(xhr.responseText);
        if(xhr.status == 200) {
            encryptedError.style.display = "none";
            encryptedSuccess.innerHTML = "<<p>DownloadUrl : <a href='" + response.fileDownloadUri + "' target='_blank'>" + response.fileDownloadUri + "</a></p>";
            encryptedSuccess.style.display = "block";
        } else {
            encryptedSuccess.style.display = "none";
            encryptedError.innerHTML = (response && response.message) || "Some Error Occurred";
        }
    }
    xhr.send();
}

