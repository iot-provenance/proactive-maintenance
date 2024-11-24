const coap = require('coap')
        req = coap.request('coap://[fd00::302:304:506:708]/test/hello')
        console.log("Client Request...")

    req.on('response' , function(res){
            res.pipe(process.stdout)
        })
    req.end()