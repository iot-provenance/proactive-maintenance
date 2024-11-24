var express = require('express');
var request = require('request');
// var app = express();
// var http = require('http').Server(app);
var http = require('http');
const url = require('url');

var mysql      = require('mysql');
var connection = mysql.createConnection({
  host     : 'localhost',
  user     : 'root',
  password : '0125527',
  database : 'iot'
});

var app = http.createServer(function(req2,res2){
    res2.setHeader('Content-Type', 'application/json');
var param="";

var node="2";

if(url.parse(req2.url,true).query!=null && url.parse(req2.url,true).query.action!=null){
  param= url.parse(req2.url,true).query.action;
}

if(url.parse(req2.url,true).query!=null && url.parse(req2.url,true).query.node!=null){
    node= url.parse(req2.url,true).query.node;
    
  }

  var urladdress="http://localhost:3000/index.html?action="+param+"&node="+node;
  console.log(urladdress);
    request.get(urladdress,function(err,res,obj){
        if(err){
            console.log(err);
            return;
        }
        console.log(obj);
        const obj2 =JSON.parse(obj)
        // console.log(obj);
       // console.log(JSON.stringify(obj2.temp));
        if(obj2.act !== undefined){
            connection.query({
                sql: 'INSERT INTO iot.actions(node_id,type, value,time) values(?,?,?,now())',
                timeout: 40000, // 40s
                values: [ node,obj2.act, obj2.battery]
            }, function (error, results, fields) {
                if(error){
                    console.log('error occured');
                    console.log(error);
                    res2.end(JSON.stringify({ sonuc: 0, hata: "hata alındı" }, null, 3));
                    return;
                }
           
                console.log('inserted actions data to MySQL');
            });
        }else{
   if(obj2.t !== undefined){
        connection.query({
            sql: 'INSERT INTO iot.values(node_id,type, value,time) values(?,"temp",?,now())',
            timeout: 40000, // 40s
            values: [ node, obj2.t]
        }, function (error, results, fields) {
            if(error){
                console.log('error occured');
                console.log(error);
                res2.end(JSON.stringify({ sonuc: 0, hata: "hata alındı" }, null, 3));
                return;
            }
            console.log('inserted temp data to MySQL');
        });
    }
    if(obj2.b !== undefined){
        connection.query({
            sql: 'INSERT INTO iot.values(node_id,type, value,time) values(?,"battery",?,now())',
            timeout: 40000, // 40s
            values: [ node, obj2.b]
        }, function (error, results, fields) {
            if(error){
                console.log('error occured');
                console.log(error);
                res2.end(JSON.stringify({ sonuc: 0, hata: "hata alındı" }, null, 3));
                return;
            }
            console.log('inserted  battery data to MySQL');
        });
    }

    if(obj2.c !== undefined){
        connection.query({
            sql: 'INSERT INTO iot.values(node_id,type, value,time) values(?,"cpu",?,now())',
            timeout: 40000, // 40s
            values: [ node, obj2.c]
        }, function (error, results, fields) {
            if(error){
                console.log('error occured');
                console.log(error);
                res2.end(JSON.stringify({ sonuc: 0, hata: "hata alındı" }, null, 3));
                return;
            }
            console.log('inserted  battery data to MySQL');
        });
    }

    if(obj2.m !== undefined){
        connection.query({
            sql: 'INSERT INTO iot.values(node_id,type, value,time) values(?,"memory",?,now())',
            timeout: 40000, // 40s
            values: [ node, obj2.m]
        }, function (error, results, fields) {
            if(error){
                console.log('error occured');
                console.log(error);
                res2.end(JSON.stringify({ sonuc: 0, hata: "hata alındı" }, null, 3));
                return;
            }
            console.log('inserted  battery data to MySQL');
        });
    }

    if(obj2.g !== undefined){
        connection.query({
            sql: 'INSERT INTO iot.values(node_id,type, value,time) values(?,"gas",?,now())',
            timeout: 40000, // 40s
            values: [ node, obj2.g]
        }, function (error, results, fields) {
            if(error){
                console.log('error occured');
                console.log(error);
                res2.end(JSON.stringify({ sonuc: 0, hata: "hata alındı" }, null, 3));
                return;
            }
            console.log('inserted  battery data to MySQL');
        });
    }

    if(obj2.p !== undefined){
        connection.query({
            sql: 'INSERT INTO iot.values(node_id,type, value,time) values(?,"pressure",?,now())',
            timeout: 40000, // 40s
            values: [ node, obj2.p]
        }, function (error, results, fields) {
            if(error){
                console.log('error occured');
                console.log(error);
                res2.end(JSON.stringify({ sonuc: 0, hata: "hata alındı" }, null, 3));
                return;
            }
            console.log('inserted  battery data to MySQL');
        });
    }


}
    res2.end(obj);

    });
});


    app.listen(3001);