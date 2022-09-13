// memory for data
let confirmedCovid = [];
let nonCovid = [];
let suspectedCovid = [];
let ventilatedCovid = [];
let publishedOnPatients = [];
let confirmedCovidWeek = [];
let nonCovidWeek = [];
let suspectedCovidWeek = [];
let ventilatedCovidWeek = [];
let publishedOnPatientsWeek = [];
let confirmedCovidMonth = [];
let nonCovidMonth = [];
let suspectedCovidMonth = [];
let ventilatedCovidMonth = [];
let publishedOnPatientsMonth = [];
let publishedOnPatientsMonthXAxis = [];

// indices for range selection
let minDatePatients = 0;
let maxDatePatients = 0;
let startIndexSlovakiaPatients = 0;
let endIndexSlovakiaPatients = 0;

let intervalPatients = 'weekly';
let regionPatients = 0;

let patientsChart = new Chart(document.getElementById("PatientsCanvas").getContext("2d"), {});

//---------------------- LISTENERS

document.getElementById("PatientsInterval").addEventListener("change", event => {
    event.preventDefault();
    intervalPatients = event.target.value;
    patientsChart.destroy();
    selectDataAndPlotHospitalPatients(intervalPatients);
});
document.getElementById("PatientsRegion").addEventListener("change", event => {
    event.preventDefault();
    regionPatients = event.target.value;
    patientsChart.destroy();
    selectDataAndPlotHospitalPatients(intervalPatients);
})

//---------------------- CALLS

initializeArrays(confirmedCovid);
initializeArrays(nonCovid);
initializeArrays(suspectedCovid);
initializeArrays(ventilatedCovid);
initializeArrays(confirmedCovidWeek);
initializeArrays(nonCovidWeek);
initializeArrays(suspectedCovidWeek);
initializeArrays(ventilatedCovidWeek);
initializeArrays(confirmedCovidMonth);
initializeArrays(nonCovidMonth);
initializeArrays(suspectedCovidMonth);
initializeArrays(ventilatedCovidMonth);

fetchHospitalPatientsData("http://localhost:8080/api/hospital-patients/in-slovakia");
fetchRegionHospitalPatients("http://localhost:8080/api/hospital-patients/by-region");

//---------------------- FUNCTIONS

async function fetchHospitalPatientsData(url) {
    await fetch(url)
        .then(response => {
            if (response.ok) {
                return response.json();
            } else {
                return Promise.reject(new Error(`Slovakia hospital patients data acquisition failed. Server answered with ${response.status}: ${response.statusText}.`));
            }
        })
        .then(data => {
            const dataLength = data.length;
            for (let i = 0; i < dataLength; i++) {
                confirmedCovid[0][i] = data[i].confirmedCovid;
                nonCovid[0][i] = data[i].nonCovid;
                suspectedCovid[0][i] = data[i].suspectedCovid;
                ventilatedCovid[0][i] = data[i].ventilatedCovid;
                publishedOnPatients[i] = data[i].publishedOn;
            }
            confirmedCovid[0].reverse();
            nonCovid[0].reverse();
            suspectedCovid[0].reverse();
            ventilatedCovid[0].reverse();
            publishedOnPatients.reverse();
        })
}

async function fetchRegionHospitalPatients(url) {
    await fetch(url)
        .then(response => {
            if (response.ok) {
                return response.json();
            } else {
                return Promise.reject(new Error(`Region hospital patients data acquisition failed. Server answered with ${response.status}: ${response.statusText}.`));
            }
        })
        .then(data => {
            minDatePatients = createDate(publishedOnPatients[0]);
            maxDatePatients = createDate(publishedOnPatients[publishedOnPatients.length - 1]);

            const dataLength = data.length;
            for (let i = 0; i < dataLength; i++) {
                let index = Math.round((createDate(data[i].publishedOn).getTime() - minDatePatients.getTime()) / millisecondsInDay);
                let region = data[i].region.id;

                confirmedCovid[region][index] = data[i].confirmedCovid;
                nonCovid[region][index] = data[i].nonCovid;
                suspectedCovid[region][index] = data[i].suspectedCovid;
                ventilatedCovid[region][index] = data[i].ventilatedCovid;
            }
            for (let region = 1; region < 9; region++) {
                confirmedCovid[region].reverse();
                nonCovid[region].reverse();
                suspectedCovid[region].reverse();
                ventilatedCovid[region].reverse();
            }
            prepareWeeklyDataPatients();
            prepareMonthlyDataPatients();

            startIndexSlovakiaPatients = 0;
            endIndexSlovakiaPatients = publishedOnPatients.length - 1;
            patientsChart.destroy();
            selectDataAndPlotHospitalPatients(intervalPatients);
            createDatePickerPatients();
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
            startIndexSlovakiaPatients = Math.round((start._d.getTime() - minDatePatients.getTime()) / millisecondsInDay);
            endIndexSlovakiaPatients = Math.round((end._d.getTime() - minDatePatients.getTime()) / millisecondsInDay);
            patientsChart.destroy();
            selectDataAndPlotHospitalPatients(intervalPatients);
        });
    });
}

function prepareWeeklyDataPatients() {
    for (let region = 0; region < 9; region++) {
        let dayIdx = 0;
        let weekIdx = 0;
        while (dayIdx < publishedOnPatients.length) {
            confirmedCovidWeek[region][weekIdx] = 0;
            nonCovidWeek[region][weekIdx] = 0;
            suspectedCovidWeek[region][weekIdx] = 0;
            ventilatedCovidWeek[region][weekIdx] = 0;
            do {
                confirmedCovidWeek[region][weekIdx] += confirmedCovid[region][dayIdx];
                nonCovidWeek[region][weekIdx] += nonCovid[region][dayIdx];
                suspectedCovidWeek[region][weekIdx] += suspectedCovid[region][dayIdx];
                ventilatedCovidWeek[region][weekIdx] += ventilatedCovid[region][dayIdx];
                dayIdx++;
            } while (dayIdx < publishedOnPatients.length && createDate(publishedOnPatients[dayIdx]).getDay() != 0);
            publishedOnPatientsWeek[weekIdx] = publishedOnPatients[dayIdx - 1];
            weekIdx++;
        }
    }
}

function prepareMonthlyDataPatients() {
    for (let region = 0; region < 9; region++) {
        let dayIdx = 0;
        let monthIdx = 0;
        while(dayIdx < publishedOnPatients.length) {
            confirmedCovidMonth[region][monthIdx] = 0;
            nonCovidMonth[region][monthIdx] = 0;
            suspectedCovidMonth[region][monthIdx] = 0;
            ventilatedCovidMonth[region][monthIdx] = 0;
            do {
                confirmedCovidMonth[region][monthIdx] += confirmedCovid[region][dayIdx];
                nonCovidMonth[region][monthIdx] += nonCovid[region][dayIdx];
                suspectedCovidMonth[region][monthIdx] += suspectedCovid[region][dayIdx];
                ventilatedCovidMonth[region][monthIdx] += ventilatedCovid[region][dayIdx];
                dayIdx++;
            } while(dayIdx < publishedOnPatients.length && createDate(publishedOnPatients[dayIdx]).getDate() != 1);
            publishedOnPatientsMonth[monthIdx] = publishedOnPatients[dayIdx - 1];
            publishedOnPatientsMonthXAxis[monthIdx] = month[createDate(publishedOnPatients[dayIdx - 1]).getMonth()] + ' ' + publishedOnPatients[dayIdx - 1].substring(0, 4);
            monthIdx++;
        }
    }
}

function selectDataAndPlotHospitalPatients(interval) {
    let confirmedCovidNew = [];
    let nonCovidNew = [];
    let suspectedCovidNew = [];
    let ventilatedCovidNew = [];
    let publishedOnPatientsNew = [];

    if (interval === 'daily') {
        for (let i = startIndexSlovakiaPatients; i < endIndexSlovakiaPatients; i++) {
            confirmedCovidNew[i - startIndexSlovakiaPatients] = confirmedCovid[regionPatients][i];
            nonCovidNew[i - startIndexSlovakiaPatients] = nonCovid[regionPatients][i];
            suspectedCovidNew[i - startIndexSlovakiaPatients] = suspectedCovid[regionPatients][i];
            ventilatedCovidNew[i - startIndexSlovakiaPatients] = ventilatedCovid[regionPatients][i];
            publishedOnPatientsNew[i - startIndexSlovakiaPatients] = publishedOnPatients[i];
        }
    } else if (interval === 'weekly') {
        let startDate = createDate(publishedOnPatients[startIndexSlovakiaPatients]);
        let endDate = createDate(publishedOnPatients[endIndexSlovakiaPatients]);

        let startIdx = -1;
        let endIdx = -1;
        for (let i = 0; i < publishedOnPatientsWeek.length; i++) {
            let curDate = createDate(publishedOnPatientsWeek[i]);
            if (startIdx === -1 && curDate >= startDate) {
                startIdx = i;
            }
            if (endIdx === -1 && curDate >= endDate) {
                endIdx = i;
                break;
            }
        }
        for (let i = startIdx; i < endIdx; i++) {
            confirmedCovidNew[i - startIndexSlovakiaPatients] = confirmedCovidWeek[regionPatients][i];
            nonCovidNew[i - startIndexSlovakiaPatients] = nonCovidWeek[regionPatients][i];
            suspectedCovidNew[i - startIndexSlovakiaPatients] = suspectedCovidWeek[regionPatients][i];
            ventilatedCovidNew[i - startIndexSlovakiaPatients] = ventilatedCovidWeek[regionPatients][i];
            publishedOnPatientsNew[i - startIndexSlovakiaPatients] = publishedOnPatientsWeek[i];
        }
    } else if (interval === 'monthly') {
        let startDate = createDate(publishedOnPatients[startIndexSlovakiaPatients]);
        let endDate = createDate(publishedOnPatients[endIndexSlovakiaPatients]);

        let startIdx = -1;
        let endIdx = -1;
        for (let i = 0; i < publishedOnPatientsMonth.length; i++) {
            let curDate = createDate(publishedOnPatientsMonth[i]);
            if (startIdx === -1 && curDate >= startDate) {
                startIdx = i;
            }
            if (endIdx === -1 && curDate >= endDate) {
                endIdx = i;
                break;
            }
        }
        for (let i = startIdx; i < endIdx; i++) {
            confirmedCovidNew[i - startIndexSlovakiaPatients] = confirmedCovidMonth[regionPatients][i];
            nonCovidNew[i - startIndexSlovakiaPatients] = nonCovidMonth[regionPatients][i];
            suspectedCovidNew[i - startIndexSlovakiaPatients] = suspectedCovidMonth[regionPatients][i];
            ventilatedCovidNew[i - startIndexSlovakiaPatients] = ventilatedCovidMonth[regionPatients][i];
            publishedOnPatientsNew[i - startIndexSlovakiaPatients] = publishedOnPatientsMonth[i];
        }
    }

    plotHospitalPatients(confirmedCovidNew, ventilatedCovidNew, suspectedCovidNew, nonCovidNew, publishedOnPatientsNew);
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
    }

    patientsChart = new Chart(document.getElementById("PatientsCanvas").getContext("2d"), {
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
    });
}