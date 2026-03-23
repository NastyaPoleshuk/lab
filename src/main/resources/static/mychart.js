document.addEventListener("DOMContentLoaded", function() {

    const bridge = document.getElementById('gas-data-bridge');
    if (!bridge) return;
    const data = {
        currentP: parseFloat(bridge.dataset.p),
        currentV: parseFloat(bridge.dataset.v),
        postP: parseFloat(bridge.dataset.postP),
        postV: parseFloat(bridge.dataset.postV),
        mass: parseFloat(bridge.dataset.mass),
        molarMass: parseFloat(bridge.dataset.molar),
        temp: parseFloat(bridge.dataset.temp),
        R: 8.31
    };
    const canvas = document.getElementById('isothermChart');
    if (!canvas) return;
    const ctx = canvas.getContext('2d');

    const processPoints = [];
    const steps = 50;

    const startV = data.currentV;
    const endV = data.postV;

    for (let i = 0; i <= steps; i++) {
        let v = startV + (endV - startV) * (i / steps);
        let p = (data.mass * data.R * data.temp) / (data.molarMass * v);
        processPoints.push({
            x: v.toFixed(4),
            y: p.toFixed(2)
        });
    }
    new Chart(ctx, {
        type: 'line',
        data: {
            datasets: [{
                label: 'Изотермический процесс (T=' + data.temp + 'K)',
                data: processPoints,
                borderColor: '#000066',
                backgroundColor: 'rgba(0, 0, 102, 0.1)',
                borderWidth: 3,
                pointRadius: 0,
                pointHitRadius: 10,
                tension: 0.4,
                fill: true
            }]
        },
        options: {
            responsive: true,
            maintainAspectRatio: false,
            scales: {
                x: {
                    type: 'linear',
                    title: {
                        display: true,
                        text: 'Объем V (м³)',
                        color: '#2c3e50',
                        font: { weight: 'bold' }
                    },
                    grid: { color: '#ecf0f1' }
                },
                y: {
                    title: {
                        display: true,
                        text: 'Давление P (Па)',
                        color: '#2c3e50',
                        font: { weight: 'bold' }
                    },
                    grid: { color: '#ecf0f1' }
                }
            },
            plugins: {
                legend: {
                    display: true,
                    position: 'top'
                },
                tooltip: {
                    callbacks: {
                        label: function(context) {
                            return `V: ${context.parsed.x} м³, P: ${context.parsed.y} Па`;
                        }
                    }
                }
            }
        }
    });
});