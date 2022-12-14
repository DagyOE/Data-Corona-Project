// indices for range selection
let minDatePatients = createDate("2020-04-30");
let maxDatePatients = new Date();
let startDatePatients = "2020-04-30";
let endDatePatients = new Date();

let intervalPatients = 'weekly';
let regionPatients = '0';

let urlSlovakia = `http://localhost:8080/api/hospital-patients/in-slovakia/${intervalPatients}/${startDatePatients}/${endDatePatients}`;
let urlRegions = `http://localhost:8080/api/hospital-patients/by-region/${intervalPatients}/${startDatePatients}/${endDatePatients}`;

let myChartPatients = new Chart(document.getElementById("PatientsCanvas").getContext("2d"), {});

//---------------------- LISTENERS

document.getElementById("PatientsInterval").addEventListener("change", event => {
    event.preventDefault();
    intervalPatients = event.target.value;

    if (regionPatients === '0')
        fetchPatientsData(`http://localhost:8080/api/hospital-patients/in-slovakia/${intervalPatients}/${startDatePatients}/${endDatePatients}`);
    else
        fetchRegionPatientsData(`http://localhost:8080/api/hospital-patients/by-region/${intervalPatients}/${startDatePatients}/${endDatePatients}`);
});
document.getElementById("PatientsRegion").addEventListener("change", event => {
    event.preventDefault();
    regionPatients = event.target.value;

    if (regionPatients === '0')
        fetchPatientsData(`http://localhost:8080/api/hospital-patients/in-slovakia/${intervalPatients}/${startDatePatients}/${endDatePatients}`);
    else
        fetchRegionPatientsData(`http://localhost:8080/api/hospital-patients/by-region/${intervalPatients}/${startDatePatients}/${endDatePatients}`);
})

//---------------------- CALLS

// findFirstAndLastRecord(`http://127.0.0.1:8080/api/hospital-patients/in-slovakia`);
fetchPatientsData(`http://localhost:8080/api/hospital-patients/in-slovakia/${intervalPatients}/${startDatePatients}/${endDatePatients}`);
createDatePickerPatients();

//---------------------- FUNCTIONS

async function fetchPatientsData(url) {
    await fetch(url)
        .then(response => {
            if (response.ok) {
                return response.json();
            } else {
                return Promise.reject(new Error(`Hospital patients data acquisition failed. Server answered with ${response.status}: ${response.statusText}.`));
            }
        })
        .then(data => {
            let confirmedCovid = [];
            let nonCovid = [];
            let suspectedCovid = [];
            let ventilatedCovid = [];
            let publishedOn = [];
            // filling memory
            const dataLength = data.length;
            for (let i = 0; i < dataLength; i++) {
                confirmedCovid[i] = data[i].confirmedCovid;
                nonCovid[i] = data[i].nonCovid;
                suspectedCovid[i] = data[i].suspectedCovid;
                ventilatedCovid[i] = data[i].ventilatedCovid;
                publishedOn[i] = data[i].publishedOn;
            }
            confirmedCovid.reverse();
            nonCovid.reverse();
            suspectedCovid.reverse();
            ventilatedCovid.reverse();
            publishedOn.reverse();

            myChartPatients.destroy();
            plotHospitalPatients(confirmedCovid, ventilatedCovid, suspectedCovid, nonCovid, publishedOn);
        })
}

async function fetchRegionPatientsData(url) {
    await fetch(url)
        .then(response => {
            if (response.ok) {
                return response.json();
            } else {
                return Promise.reject(new Error(`Region hospital patients data acquisition failed. Server answered with ${response.status}: ${response.statusText}.`));
            }
        })
        .then(data => {
            let confirmedCovid = [];
            let nonCovid = [];
            let suspectedCovid = [];
            let ventilatedCovid = [];
            let publishedOn = [];
            // filling memory
            let indexRegion = 0;
            const dataLength = data.length;
            for (let i = 0; i < dataLength; i++) {
                let regionData = data[i].region.id.toString();

                if (regionData === regionPatients) {
                    confirmedCovid[indexRegion] = data[i].confirmedCovid;
                    nonCovid[indexRegion] = data[i].nonCovid;
                    suspectedCovid[indexRegion] = data[i].suspectedCovid;
                    ventilatedCovid[indexRegion] = data[i].ventilatedCovid;
                    publishedOn[indexRegion] = data[i].publishedOn;
                    indexRegion++;
                }
            }
            if (intervalPatients === 'daily') {
                confirmedCovid.reverse();
                nonCovid.reverse();
                suspectedCovid.reverse();
                ventilatedCovid.reverse();
                publishedOn.reverse();
            }
            myChartPatients.destroy();
            plotHospitalPatients(confirmedCovid, ventilatedCovid, suspectedCovid, nonCovid, publishedOn);
        })
}

async function findFirstAndLastRecord(url) {
    await fetch(url)
        .then(response => {
            if (response.ok) {
                return response.json();
            } else {
                return Promise.reject(new Error(`Slovakia vaccination data acquisition failed. Server answered with ${response.status}: ${response.statusText}.`));
            }
        })
        .then(data => {
            endDatePatients = deconstructDateToUrl(createDate(data[0].publishedOn));
            startDatePatients = deconstructDateToUrl(createDate(data[data.length - 1].publishedOn));
        })
}

function createDatePickerPatients() {
    $(function() {
        $('input[name="PatientsRange"]').daterangepicker({
            opens: 'left',
            minDate: minDatePatients,
            maxDate: maxDatePatients,
            startDate: minDatePatients,
            endDate: maxDatePatients
        }, function(start, end, label) {
            minDatePatients = start._d;
            startDatePatients = deconstructDateToUrl(start._d);
            endDatePatients = deconstructDateToUrl(end._d);

            if (regionPatients === '0')
                fetchPatientsData(`http://localhost:8080/api/hospital-patients/in-slovakia/${intervalPatients}/${startDatePatients}/${endDatePatients}`);
            else
                fetchRegionPatientsData(`http://localhost:8080/api/hospital-patients/by-region/${intervalPatients}/${startDatePatients}/${endDatePatients}`);
        });
    });
}

function plotHospitalPatients(confirmedCovid, ventilatedCovid, suspectedCovid, nonCovid, publishedOn) {
    const data = {
        labels: publishedOn,
        datasets: [
            {
                label: "Covidov?? pacienti",
                type: 'bar',
                backgroundColor: BACKGROUND_CHART_COLORS.red,
                borderColor: CHART_COLORS.red,
                borderWidth: 2,
                data: confirmedCovid
            },
            {
                label: "Covidov?? pacienti na ventil??cii",
                type: 'bar',
                backgroundColor: BACKGROUND_CHART_COLORS.blue,
                borderColor: CHART_COLORS.blue,
                borderWidth: 2,
                data: ventilatedCovid
            },
            {
                label: "Pacienti s podozren??m na Covid",
                type: 'bar',
                backgroundColor: BACKGROUND_CHART_COLORS.green,
                borderColor: CHART_COLORS.green,
                borderWidth: 2,
                data: suspectedCovid
            },
            {
                label: "Necovidov?? pacienti",
                type: 'bar',
                backgroundColor: BACKGROUND_CHART_COLORS.orange,
                borderColor: CHART_COLORS.orange,
                borderWidth: 2,
                data: nonCovid
            }
        ]
    };

    myChartPatients = new Chart(document.getElementById("PatientsCanvas").getContext("2d"), {
        type: 'scatter',
        data: data,
        options: {
            data: data,
            options: {
                plugins: {
                    title: {
                        display: true,
                        text: "Pacienti v nemocniciach"
                    },
                    tooltip: {
                        callbacks: {
                            label: (tooltipItem, data) => (`${myChartPatients.data.datasets[tooltipItem.datasetIndex].data[tooltipItem.dataIndex]}`)
                        }
                    }
                },
                hover: {
                    mode: 'point',
                },
                responsive: true
            }
        }
    });
}