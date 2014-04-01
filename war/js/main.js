$(document).ready(function(){
  $('body').fixedScroll({
    // or provide a list of background images or colors
    'backgrounds': [
        'images/concert_1.jpg',
        'images/concert_2.jpg',
        'images/headphones.jpg',
       'images/soundbox.jpg', 
    ],
    // also specify the number of sections manually with 'numSections'
    'numSections': 2,
    // use 'sectionContent' to override what shows up in each section by default
    'sectionContent': '' +
        '<div class="contentwrap">' +
          '<div class="content">' +
          '</div>' +
        '</div>'
  });
  // move the display content into the first section
  $('#section0 .content').empty()
  //$('#sec1').appendTo('#section0 .content');
  $('#sec1').appendTo('#section3 .content');

});
