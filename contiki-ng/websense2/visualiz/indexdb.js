var express = require('express');
var request = require('request');
var app = express();
var http = require('http').Server(app);

var mysql      = require('mysql');
var connection = mysql.createConnection({
  host     : 'localhost',
  user     : 'root',
  password : '0125527',
  database : 'iot'
});



    request.get('http://localhost:3000/',function(err,res,obj){
        if(err){
            console.log(err);
            return;
        }
        console.log(obj);
        const obj2 =JSON.parse(obj)
        // console.log(obj);
        console.log(JSON.stringify(obj2.temp));
   if(obj2.temp !== undefined){
        connection.query({
            sql: 'INSERT INTO iot.values(node_id,type, value,time) values(1,"temp",?,now())',
            timeout: 40000, // 40s
            values: [ obj2.temp]
        }, function (error, results, fields) {
            if(error){
                console.log('error occured');
                console.log(error);
                return;
            }
            console.log('inserted data to MySQL');
        });
    }
    });


