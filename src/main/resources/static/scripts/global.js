'use strict';
if (!window.console) {
    window.console = {
        log: function (info) {

        }
    }
}

Array.prototype.shuffle = function () {
    for (var rnd, tmp, i = this.length; i; rnd = parseInt(Math.random() * i), tmp = this[--i], this[i] = this[rnd], this[rnd] = tmp)
        ;
    return this;
};

//去除左右全角半角空格
String.prototype.newtrim = function () {
    var strTrim = this.replace(/(^\s*)|(\s*$)/g, "");
    strTrim = strTrim.replace(/^[\s　\t]+|[\s　\t]+$/, "");
    strTrim = strTrim.replace(/(^\s*)|(\s*$)/g, "");
    strTrim = strTrim.replace(/^[\s　\t]+|[\s　\t]+$/, "");
    return strTrim;
};

String.prototype.startWith = function (str) {
    var reg = new RegExp("^" + str);
    return reg.test(this);
};

var global = {
    "getContextPath": function () {
        return global_ctxPath.substring(0, global_ctxPath.length - 1);
    },
    'getType': function (o) {
        return Object.prototype.toString.call(o);
    },
    "trim": function (value) {
        if (value == null) {
            return ''
        } else {
            var strTrim = value.replace(/(^\s*)|(\s*$)/g, "");
            strTrim = strTrim.replace(/^[\s　\t]+|[\s　\t]+$/, "");
            strTrim = strTrim.replace(/(^\s*)|(\s*$)/g, "");
            strTrim = strTrim.replace(/^[\s　\t]+|[\s　\t]+$/, "");
            return strTrim;
        }
    },
    "defaultIfBlack": function (str, value) {
        if (str === undefined || str == null || str == '') {
            if (value !== undefined) {
                return value;
            } else {
                return null;
            }
        } else {
            return str;
        }
    },
    "clen": function (str) {
        str = str + "";
        return str.match(/[^ -~]/g) == null ? str.length : str.length + str.match(/[^ -~]/g).length;
    },
    "encode": function (unencoded) {
        return encodeURIComponent(unencoded).replace(/'/g, "%27").replace(/"/g, "%22");
    },
    "decode": function (encoded) {
        return decodeURIComponent(encoded.replace(/\+/g, " "));
    },
    "htmlEncode": function (html) {
        if (html && html.length > 0)
            return html.replace(/&/g, "&amp;").replace(/</g, "&lt;").replace(/>/g, "&gt;").replace(/ /g, "&nbsp;").replace(/\'/g, "&#39;").replace(/\"/g, "&quot;").replace(/\n/g, "<br>");
        else
            return "";
    },
    "htmlDecode": function (text) {
        if (text && text.length > 0)
            return text.replace(/&amp;/g, "&").replace(/&lt;/g, "<").replace(/&gt;/g, ">").replace(/&nbsp;/g, " ").replace(/&#39;/g, "\'").replace(/&quot;/g, "\"").replace("<br>", "\n");
        else
            return "";
    },
    "uuid4": function () {
        return Math.floor((1 + Math.random()) * 0x10000)
            .toString(16)
            .substring(1);
    },
    "uuid8": function () {
        return global.uuid4() + global.uuid4();
    },
    "uuid16": function () {
        return global.uuid8() + global.uuid8();
    },
    "uuid": function () {
        return global.uuid8() + '-' + global.uuid4() + '-' + global.uuid4() + '-' + global.uuid4() + '-' + global.uuid16();
    },
    "queryString": function () {
        var query_string = {};
        var query = window.location.search.substring(1);
        var vars = query.split("&");
        for (var i = 0; i < vars.length; i++) {
            var pair = vars[i].split("=");
            if (typeof query_string[pair[0]] === "undefined") {
                query_string[pair[0]] = pair[1];
            } else if (typeof query_string[pair[0]] === "string") {
                var arr = [query_string[pair[0]], pair[1]];
                query_string[pair[0]] = arr;
            } else {
                query_string[pair[0]].push(pair[1]);
            }
        }
        return query_string;
    },
    /**
     * Id校验,根据UUID生成的形式,校验长度为32为包含数字与小写字母
     * */
    "verifyId":function(val){
        var regex = /^(?=.*[a-z])(?=.*[0-9])[a-z0-9]{32}$/;
        if(regex.test(val)){
            return true;
        }else{
            return false;
        }
    },
    /**
     * 校验长度,汉字两个字符,不能为空
     * */
    "verifyLengthNotEmpty":function(inputName,val,size,index){
        var len = 0;
        for(var i=0;i<val.length;i++){
            var a = val.charAt(i);
            if(a.match(/[^\x00-\xff]/ig)!=null){
                len += 2;
            }else{
                len += 1;
            }
        }
        index.text('');
        if(len>size||len==0){
            index.text(inputName+"长度不能为空且不能大于"+size+"个字符！");
            return false;
        }else{
            return true;
        }
    },
    /**
     * 校验长度,汉字两个字符,不能为空
     * */
    "verifyLengthCanEmpty":function(inputName,val,size,index) {
        var len = 0;
        for (var i = 0; i < val.length; i++) {
            var a = val.charAt(i);
            if (a.match(/[^\x00-\xff]/ig) != null) {
                len += 2;
            } else {
                len += 1;
            }
        }
        index.text('');
        if (len > size) {
            index.text(inputName + "长度不能大于" + size + "个字符！");
            return false;
        } else {
            return true;
        }
    }
};