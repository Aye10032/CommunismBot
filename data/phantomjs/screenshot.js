/**
 * phantomJs 脚本
 */
var page = require('webpage').create(), system = require('system'), address, output, timeout, size;

if (system.args.length < 4 || system.args.length > 6) {
    phantom.exit(1);
} else {
    address = system.args[1];
    output = system.args[2];
    timeout = system.args[3];
    //定义宽高
    page.viewportSize = {
        width : 1366,
        height : 1080
    };

    page.customHeaders = {
        "Content-Type": "application/json",
        "User-Agent": "User-Agent:Mozilla/5.0 (Windows NT 10.0; WOW64; rv:38.0) Gecko/20100101 Firefox/38.0"
    };

    page.open(address, function(status) {
        window.setTimeout(function() {

            var bb = page.evaluate(function() {
                window.scrollTo(0,2000);
                return document.getElementsByTagName('html')[0].getBoundingClientRect();
            });
            //page.clipRect = {
            //    top : bb.top,
            //    left : bb.left,
            //    width : bb.width,
            //    height : bb.height
            //};
            page.render(output);
            page.close();
            console.log('渲染成功...');
            phantom.exit(0);
        }, timeout);
    });
}
