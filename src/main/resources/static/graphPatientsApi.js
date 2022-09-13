// indices for range selection
let minDatePatients = createDate("2021-01-04");
let maxDatePatients = createDate("2022-07-26");
let startDatePatients = deconstructDateToUrl(createDate("2021-01-04"));
let endDatePatients = deconstructDateToUrl(createDate("2022-07-26"));

let intervalPatients = 'daily';
let regionPatients = 0;

let myChartPatients = new Chart(document.getElementById("PatientsCanvas").getContext("2d"), {});

//---------------------- LISTENERS

document.getElementById("PatientsInterval").addEventListener("change", event => {
    event.preventDefault();
    intervalPatients = event.target.value;

    if (regionPatients == 0)
        fetchVaccinationData(`http://localhost:8080/hospital-patients/in-slovakia/${intervalPatients}/${startDatePatients}/${endDatePatients}`);
    else
        fetchRegionVaccinationData(`http://localhost:8080/api/hospital-patients/by-region/${intervalPatients}/${startDatePatients}/${endDatePatients}`);
});
document.getElementById("PatientsRegion").addEventListener("change", event => {
    event.preventDefault();
    regionPatients = event.target.value;

    if (regionPatients == 0)
        fetchVaccinationData(`http://localhost:8080/api/hospital-patients/in-slovakia/${intervalPatients}/${startDatePatients}/${endDatePatients}`);
    else
        fetchRegionVaccinationData(`http://localhost:8080/api/hospital-patients/by-region/${intervalPatients}/${startDatePatients}/${endDatePatients}`);
})

//---------------------- CALLS

findFirstAndLastRecord(`http://localhost:8080/api/hospital-patients/in-slovakia`);
fetchVaccinationData(`http://localhost:8080/api/hospital-patients/in-slovakia/${intervalPatients}/${startDatePatients}/${endDatePatients}`);
createDatePickerSlovakia();

//---------------------- FUNCTIONS

async function fetchVaccinationData(url) {
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
            let publishedOnPatients = [];
            initializeArrays(confirmedCovid);
            initializeArrays(nonCovid);
            initializeArrays(suspectedCovid);
            initializeArrays(ventilatedCovid);
            // filling memory
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

            myChartPatients.destroy();
            plotSlovakiaVaccinations(confirmedCovid[region], nonCovid[region], suspectedCovid[region], ventilatedCovid[region], publishedOnPatients);
        })
}

async function fetchRegionVaccinationData(url) {
    await fetch(url)
        .then(response => {
            if (response.ok) {
                return response.json();
            } else {
                return Promise.reject(new Error(`Region hospital patients data acquisition failed. Server answered with ${response.status}: ${response.statusText}.`));
            }
        })
        .then(data => {
            let dose1Count = [];
            let dose2Count = [];
            let dose1Sum = [];
            let dose2Sum = [];
            let publishedOn = [];
            initializeArrays(dose1Count);
            initializeArrays(dose2Count);
            initializeArrays(dose1Sum);
            initializeArrays(dose2Sum);
            // filling memory
            const dataLength = data.length;
            for (let i = 0; i < dataLength; i++) {
                let index = Math.round((createDate(data[i].publishedOn).getTime() - minDate.getTime()) / millisecondsInDay);
                let region = data[i].region.id;

                dose1Count[region][index] = data[i].dose1Count;
                dose2Count[region][index] = data[i].dose2Count;
                dose1Sum[region][index] = data[i].dose1Sum;
                dose2Sum[region][index] = data[i].dose2Sum;
                publishedOn[index] = data[i].publishedOn;
            }
            fillArraysWithPreviousDataPoint(dose1Sum, dose2Sum, publishedOn);
            myChartPatients.destroy();
            plotSlovakiaVaccinations(dose1Count[region], dose2Count[region], dose1Sum[region], dose2Sum[region], publishedOn);
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
            endDate = deconstructDateToUrl(createDate(data[0].publishedOn));
            startDate = deconstructDateToUrl(createDate(data[data.length - 1].publishedOn));
        })
}

function fillArraysWithPreviousDataPoint(dose1Sum, dose2Sum, publishedOn) {
    const length = publishedOn.length;
    for (let region = 1; region < 9; region++) {
        for (let idx = 1; idx < length; idx++) {
            if (dose1Sum[region][idx] === undefined) {
                dose1Sum[region][idx] = dose1Sum[region][idx - 1];
            }
            if (dose2Sum[region][idx] === undefined) {
                dose2Sum[region][idx] = dose2Sum[region][idx - 1];
            }
        }
    }
}

function createDatePickerSlovakia() {
    $(function() {
        $('input[name="SlovakiaVaccinationsRange"]').daterangepicker({
            opens: 'left',
            minDate: createDate("2021-01-04"),
            maxDate: createDate("2022-07-26"),
            startDate: minDatePatients,
            endDate: maxDatePatients
        }, function(start, end, label) {
            minDatePatients = start._d;
            startDatePatients = deconstructDateToUrl(start._d);
            endDatePatients = deconstructDateToUrl(end._d);

            if (regionPatients == 0)
                fetchVaccinationData(`http://localhost:8080/api/hospital-patients/in-slovakia/${intervalPatients}/${startDatePatients}/${endDatePatients}`);
            else
                fetchRegionVaccinationData(`http://localhost:8080/api/hospital-patients/by-region/${intervalPatients}/${startDatePatients}/${endDatePatients}`);
        });
    });
}

// function deconstructDateToUrl(date) {
//     dateSplit = date.toString().split(" ");
//     monthNumber = '0';
//     switch(dateSplit[1]) {
//         case "Jan": monthNumber = '01'; break;
//         case "Feb": monthNumber = '02'; break;
//         case "Mar": monthNumber = '03'; break;
//         case "Apr": monthNumber = '04'; break;
//         case "May": monthNumber = '05'; break;
//         case "Jun": monthNumber = '06'; break;
//         case "Jul": monthNumber = '07'; break;
//         case "Aug": monthNumber = '08'; break;
//         case "Sep": monthNumber = '09'; break;
//         case "Oct": monthNumber = '10'; break;
//         case "Nov": monthNumber = '11'; break;
//         case "Dec": monthNumber = '12'; break;
//     }
//     url = dateSplit[3] + "-" + monthNumber + "-" + dateSplit[2];
//     return url;
// }

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