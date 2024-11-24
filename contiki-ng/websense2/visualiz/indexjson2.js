
var http = require('http');
// var express = require('express');
var request = require('request');
const url = require('url');

var app = http.createServer(function(req,res){
res.setHeader('Content-Type', 'application/json');
var param="";
var address="http://[fd00::212:7402:2:202]/";

if(url.parse(req.url,true).query!=null && url.parse(req.url,true).query.action!=null){
  param= url.parse(req.url,true).query.action;
}

if(url.parse(req.url,true).query!=null && url.parse(req.url,true).query.node!=null){
  node= url.parse(req.url,true).query.node;

  if(node!=undefined && !isNaN(node)){
address="http://[fd00::212:740"+node+":"+node+":"+node+"0"+node+"]/";
;  }
}

console.log(address);
request.get(address+param,function(err2,res2,body2){
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
