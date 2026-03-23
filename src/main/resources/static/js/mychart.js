document.addEventListener("DOMContentLoaded", function() {
    const data = window.gasData;

    if (data && data.currentP > 0) {
        const ctx = document.getElementById('isothermChart').getContext('2d');

        const processPoints = [];
        const startV = data.currentV;
        const endV = data.postV;
        const steps = 40;

        for (let i = 0; i <= steps; i++) {
            let v = startV + (endV - startV) * (i / steps);
            let p = (data.mass * data.R * data.temp) / (data.molarMass * v);
            processPoints.push({ x: v.toFixed(4), y: p.toFixed(2) });
        }

        new Chart(ctx, {
            type: 'line',
            data: {
                datasets: [{
                    label: 'Значение давления в точке',
                    data: processPoints,
                    borderColor: '#000066',
                    tension: 0.4,
                    fill: false
                }]
            },
            options: {
                scales: {
                    x: { type: 'linear', title: { display: true, text: 'V (м³)' } },
                    y: { title: { display: true, text: 'P (Па)' } }
                },
                plugins: {
                    legend: {
                        display: false
                    }
                }
            }
        });
    }
});