Full documentation can be found at literallycanvas.com.

Literally Canvas depends on jQuery (tested on 1.8.2) and underscore.js (tested
on 1.4.2).

Add the files under `css/` and `img/` to your project, as well as the
appropriate file from `js/`. Then do this:

<div class="literally"><canvas></canvas></div>

<script type="text/javascript">
  $(document).ready(function() {
    // the only LC-specific thing we have to do
    $('.literally').literallycanvas({imageURLPrefix: '/path/to/img'});

    // you may want to disable scrolling on touch devices
    $(document).bind('touchmove', function(e) {
      if (e.target === document.documentElement) {
        return e.preventDefault();
      }
    });
  });
</script>
