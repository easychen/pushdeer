const config = require("./config");
const path = require("path");
const fs = require("fs");
const express = require("express");
const app = express();
const axios = require("axios");

app.use(express.json());
app.use("/", express.static(path.join(__dirname, "./public")));
app.use(require("cors")());
app.use(require("compression")());

const httpServer = require("http").createServer(app);
const SocketIOServer = require("socket.io");
const { instrument } = require("@socket.io/admin-ui");
const io = SocketIOServer(httpServer, {
    cors: {
        origin: "*",
        credentials: false,
    },
});
instrument(io, {
    auth: false,
});
app.get("/admin", (req, res) => {
    fs.readFile("./public/index.html", (err, data) => {
        res.write(data);
        res.end();
    });
});

const checkCookieToken = function (userId, cookieToken, clientId) {
    return true;
};

function checkServerToken(serverToken) {
    return true;
}

let clientIdToUserId = {};
let userIdToClient = {}; // user-${userId}
io.on("connection", (socket) => {
    console.log(new Date(), "client connected: ", `${io.engine.clientsCount}`);
    socket.join("unauthed");

    socket.on("disconnect", () => {
        console.log(new Date(), "client disconnect: ", `${io.engine.clientsCount}`);
        if (Object.keys(clientIdToUserId).includes(socket.id)) {
            let userId = clientIdToUserId[socket.id];
            let key = `user-${userId}`;
            if (Object.keys(userIdToClient).includes(key)) {
                delete userIdToClient[key];
            }
            delete clientIdToUserId[socket.id];
        }
    });
    socket.on("register", (data) => {
        let deviceId = data.deviceId || null;
        let authToken = data.token || null;
        if (deviceId && authToken) {
            if (checkCookieToken(deviceId, authToken, socket.id)) {
                // authed
                socket.leave("unauthed");

                // axios.post(
                //     config.mainServerUrl,
                //     { userId }
                // ).then(res => {
                //     if (res.status != 200) {
                //         console.log(`状态码: ${res.status}`);
                //         return;
                //     }
                // }).catch(error => {
                //     console.error(error)
                // });


                socket.emit("register-result", "authed");
            } else {
                socket.emit("register-result", "not authed");
                // not authed
            }
        }
    });
});

app.get("/online-count", (req, res, next) => {
    let oc = io.engine.clientsCount;
    res.json({
        status: 200,
        message: "OK",
        data: {
            online_count: oc.length,
        },
    });
});

app.post(
    "notify",
    (req, res, next) => {
        if (req.headers.mainServerAuthToken != config.mainServerAuthToken) {
            res.status(403).json({
                status: 403,
                message: "unauthed",
                data: {},
            });
        } else {
            next();
        }
    },
    (req, res) => {
        if (Object.keys(clientList).includes(req.body.clientId)) {
            clientList[req.body.clientId].socket.emit("notify", req.body.notify);
            res.json({
                status: 200,
                message: "OK",
                data: {},
            });
            res.end();
        } else {
            res.status(404).json({
                status: 404,
                message: "Not Found",
                data: {},
            });
        }
    }
);

httpServer.listen(config.port, (err) => {
    if (err) {
        console.log(err);
    }
    config.hosts.forEach((host) => {
        console.log(`http://${host}:${config.port}`);
    });
});