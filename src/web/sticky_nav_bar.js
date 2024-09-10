window.onscroll = function() {toggleStickyNavbar()};

    var navbar = document.querySelector(".navbar");
    var logoSection = document.querySelector(".container-logo");
    var sticky = navbar.offsetTop;

    function toggleStickyNavbar() {
        if (window.pageYOffset > sticky) {
            navbar.classList.add("sticky-navbar");
            logoSection.classList.add("hidden-logo");
            document.body.classList.add("sticky-padding");
        } else {
            navbar.classList.remove("sticky-navbar");
            logoSection.classList.remove("hidden-logo");
            document.body.classList.remove("sticky-padding");
        }
    }