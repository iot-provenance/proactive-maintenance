
var http = require('http');
// var express = require('express');
var request = require('request');

var app = http.createServer(function(req,res){
res.setHeader('Content-Type', 'application/json');

request.get('http://[fd00::212:7402:2:202]/',function(err2,res2,body2){
            if(err2){
              console.log(err2);
              res.end(JSON.stringify({ sonuc: 0, hata: "hata alındı" }, null, 3));
          }
else{            
    res.end(body2);

  //  res.end(JSON.stringify({ a: 1 }, null, 3));

}
         
});
});

app.listen(3000);
