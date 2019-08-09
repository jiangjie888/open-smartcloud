$(document).ready(function () {
    var page = {
        init: function () {
            page.autoSize();
            page.initControl();
            page.eventBind();
        },
        autoSize: function () {
        },
        eventBind: function () {
            //页面Resize
            $(window).resize(function () {
                page.autoSize();
            });

            $('#username').focus();

            $('#login_button').click(function (e) {
                e.preventDefault();
                if ($('#username').val() === "") {
                    alert('用户名不允许为空');
                    $('#username').focus();
                    return;
                }
                if ($('#password').val() === "") {
                    alert('用户密码不允许为空');
                    $('#password').focus();
                    return;
                }
                $.ajax({
                    type: 'post',
                    dataType: "json",
                    contentType: 'application/json',
                    url: '/oauth/authorize',

                    async: true,
                    data: JSON.stringify({ "username": $("#username").val(), "password": $("#password").val() }),
                    success: function (response) {
                        if (response.responseJSON.success) {
                            log.console(response);
                        } else {
                            alert(response.responseJSON.message);
                            return;
                        }
                    },
                    error: function (response) {
                        alert(response.responseJSON.message);
                    }
                });
            });


            //绑定Enter事件
            document.onkeydown = function (_event) {
                _event = (_event) ? _event : ((window.event) ? window.event : "");
                var k = _event.keyCode ? _event.keyCode : _event.which;
                if (k === 13) { //判断是否为回车
                    $('#login_button').trigger("click");
                }
            };

        },
        initControl: function () {
        }
    };
    //页面初始化
    page.init();
});