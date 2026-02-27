const express = require('express');
const axios = require('axios');
const app = express();

// Configuration: Port 80 is standard for web containers
const PORT = process.env.PORT || 80;
// Internal K8s DNS name for the Java service
const BACKEND_URL = process.env.BACKEND_URL || 'http://ip-echo-service:8088';

app.set('view engine', 'ejs');

// Main Route
app.get('/', async (req, res) => {
    try {
        // SRC Call: Frontend server talks to Java backend
        const response = await axios.get(BACKEND_URL, { timeout: 3000 });
        
        res.render('index', { 
            ip: response.data.ip, 
            status: 'Connected', 
            backend: BACKEND_URL,
            error: null 
        });
    } catch (err) {
        console.error('SRC Error:', err.message);
        res.render('index', { 
            ip: '0.0.0.0', 
            status: 'Disconnected', 
            backend: BACKEND_URL,
            error: 'Cannot reach the Java Backend Service' 
        });
    }
});

app.listen(PORT, () => {
    console.log(`Frontend Visualizer active on port ${PORT}`);
    console.log(`Routing SRC requests to: ${BACKEND_URL}`);
});
