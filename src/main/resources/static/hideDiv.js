function hideDiv() {
    var toHide = document.getElementById("hideMsg");
   
    if (toHide) {
        setTimeout(function() {
            toHide.style.display = "none";
        }, 5000);
    }
}

document.addEventListener("DOMContentLoaded", hideDiv);
