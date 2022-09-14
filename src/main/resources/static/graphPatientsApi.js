// indices for range selection
let minDatePatients = createDate("2020-04-30");
//todo: check whether it shouldn't be until today
let maxDatePatients = createDate("2022-09-08");
let startDatePatients = "2020-04-30";
//todo: check whether it shouldn't be until today
let endDatePatients = "2022-09-08";

let intervalPatients = 'daily';
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

findFirstAndLastRecord(`http://127.0.0.1:8080/api/hospital-patients/in-slovakia`);
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
            initializeArrays(confirmedCovid);
            initializeArrays(nonCovid);
            initializeArrays(suspectedCovid);
            initializeArrays(ventilatedCovid);
            // filling memory
            const dataLength = data.length;
            for (let i = 0; i < dataLength; i++) {
                let index = Math.round((createDate(data[i].publishedOn).getTime() - minDatePatients.getTime()) / millisecondsInDay);
                let region = data[i].region.id;

                confirmedCovid[region][index] = data[i].confirmedCovid;
                nonCovid[region][index] = data[i].nonCovid;
                suspectedCovid[region][index] = data[i].suspectedCovid;
                ventilatedCovid[region][index] = data[i].ventilatedCovid;
                publishedOn[index] = data[i].publishedOn;
            }
            myChartPatients.destroy();
            plotHospitalPatients(confirmedCovid[regionPatients], ventilatedCovid[regionPatients], suspectedCovid[regionPatients], nonCovid[regionPatients], publishedOn);
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
                label: "Covidoví pacienti",
                type: 'bar',
                backgroundColor: BACKGROUND_CHART_COLORS.red,
                borderColor: CHART_COLORS.red,
                borderWidth: 2,
                data: confirmedCovid
            },
            {
                label: "Covidoví pacienti na ventilácii",
                type: 'bar',
                backgroundColor: BACKGROUND_CHART_COLORS.blue,
                borderColor: CHART_COLORS.blue,
                borderWidth: 2,
                data: ventilatedCovid
            },
            {
                label: "Pacienti s podozrením na Covid",
                type: 'bar',
                backgroundColor: BACKGROUND_CHART_COLORS.green,
                borderColor: CHART_COLORS.green,
                borderWidth: 2,
                data: suspectedCovid
            },
            {
                label: "Necovidoví pacienti",
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
                            label: (tooltipItem, data) => (`${patientsChart.data.datasets[tooltipItem.datasetIndex].data[tooltipItem.dataIndex]}`)
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