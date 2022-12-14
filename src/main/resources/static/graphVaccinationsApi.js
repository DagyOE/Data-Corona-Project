// indices for range selection
let minDate = createDate("2021-01-04");
let maxDate = new Date();
let startDate = "2021-01-04";
let endDate = new Date();

let interval = 'weekly';
let region = '0';

let myChart = new Chart(document.getElementById("SlovakiaVaccinationsCanvas").getContext("2d"), {});

//---------------------- LISTENERS

document.getElementById("SlovakiaVaccinationsInterval").addEventListener("change", event => {
    event.preventDefault();
    interval = event.target.value;

    if (region === '0')
        fetchVaccinationData(`http://localhost:8080/api/vaccinations/in-slovakia/${interval}/${startDate}/${endDate}`);
    else
        fetchRegionVaccinationData(`http://localhost:8080/api/vaccinations/by-region/${interval}/${startDate}/${endDate}`);
});
document.getElementById("SlovakiaVaccinationsRegion").addEventListener("change", event => {
    event.preventDefault();
    region = event.target.value;

    if (region === '0')
        fetchVaccinationData(`http://localhost:8080/api/vaccinations/in-slovakia/${interval}/${startDate}/${endDate}`);
    else
        fetchRegionVaccinationData(`http://localhost:8080/api/vaccinations/by-region/${interval}/${startDate}/${endDate}`);
})

//---------------------- CALLS

// findFirstAndLastRecord(`http://localhost:8080/api/vaccinations/in-slovakia`);
fetchVaccinationData(`http://localhost:8080/api/vaccinations/in-slovakia/${interval}/${startDate}/${endDate}`);
createDatePickerSlovakia();

//---------------------- FUNCTIONS

async function fetchVaccinationData(url) {
    await fetch(url)
        .then(response => {
            if (response.ok) {
                return response.json();
            } else {
                return Promise.reject(new Error(`Vaccination data acquisition failed. Server answered with ${response.status}: ${response.statusText}.`));
            }
        })
        .then(data => {
            let dose1Count = [];
            let dose2Count = [];
            let dose1Sum = [];
            let dose2Sum = [];
            let publishedOn = [];
            // filling memory
            const dataLength = data.length;
            for (let i = 0; i < dataLength; i++) {
                dose1Count[i] = data[i].dose1Count;
                dose2Count[i] = data[i].dose2Count;
                dose1Sum[i] = data[i].dose1Sum;
                dose2Sum[i] = data[i].dose2Sum;
                publishedOn[i] = data[i].publishedOn;
            }
            dose1Count.reverse();
            dose2Count.reverse();
            dose1Sum.reverse();
            dose2Sum.reverse();
            publishedOn.reverse();

            myChart.destroy();
            plotSlovakiaVaccinations(dose1Count, dose2Count, dose1Sum, dose2Sum, publishedOn);
        })
}

async function fetchRegionVaccinationData(url) {
    await fetch(url)
        .then(response => {
            if (response.ok) {
                return response.json();
            } else {
                return Promise.reject(new Error(`Region vaccination data acquisition failed. Server answered with ${response.status}: ${response.statusText}.`));
            }
        })
        .then(data => {
            let dose1Count = [];
            let dose2Count = [];
            let dose1Sum = [];
            let dose2Sum = [];
            let publishedOn = [];
            const dataLength = data.length;
            let indexRegion = 0;
            for (let i = 0; i < dataLength; i++) {
                let regionData = data[i].region.id.toString();
                if (regionData === region) {
                    dose1Count[indexRegion] = data[i].dose1Count;
                    dose2Count[indexRegion] = data[i].dose2Count;
                    dose1Sum[indexRegion] = data[i].dose1Sum;
                    dose2Sum[indexRegion] = data[i].dose2Sum;
                    publishedOn[indexRegion] = data[i].publishedOn;
                    indexRegion++;
                }
            }
            if (interval === 'daily') {
                dose1Count.reverse();
                dose2Count.reverse();
                dose1Sum.reverse();
                dose2Sum.reverse();
                publishedOn.reverse();
                fillArraysWithPreviousDataPoint(dose1Sum, dose2Sum, publishedOn);
            }
            myChart.destroy();
            plotSlovakiaVaccinations(dose1Count, dose2Count, dose1Sum, dose2Sum, publishedOn);
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
            minDate: minDate,
            maxDate: maxDate,
            startDate: minDate,
            endDate: maxDate
        }, function(start, end, label) {
            minDate = start._d;
            startDate = deconstructDateToUrl(start._d);
            endDate = deconstructDateToUrl(end._d);

            if (region === '0')
                fetchVaccinationData(`http://localhost:8080/api/vaccinations/in-slovakia/${interval}/${startDate}/${endDate}`);
            else
                fetchRegionVaccinationData(`http://localhost:8080/api/vaccinations/by-region/${interval}/${startDate}/${endDate}`);
        });
    });
}

function deconstructDateToUrl(date) {
    dateSplit = date.toString().split(" ");
    monthNumber = '0';
    switch(dateSplit[1]) {
        case "Jan": monthNumber = '01'; break;
        case "Feb": monthNumber = '02'; break;
        case "Mar": monthNumber = '03'; break;
        case "Apr": monthNumber = '04'; break;
        case "May": monthNumber = '05'; break;
        case "Jun": monthNumber = '06'; break;
        case "Jul": monthNumber = '07'; break;
        case "Aug": monthNumber = '08'; break;
        case "Sep": monthNumber = '09'; break;
        case "Oct": monthNumber = '10'; break;
        case "Nov": monthNumber = '11'; break;
        case "Dec": monthNumber = '12'; break;
    }
    url = dateSplit[3] + "-" + monthNumber + "-" + dateSplit[2];
    return url;
}

function plotSlovakiaVaccinations(dose1Count, dose2Count, dose1Sum, dose2Sum, publishedOn) {
    const data = {
        labels: publishedOn,
        datasets: [
            {
                type: 'bar',
                label: "1. d??vka",
                yAxisID: 'Dose',
                backgroundColor: BACKGROUND_CHART_COLORS.red,
                borderColor: CHART_COLORS.red,
                borderWidth: 2,
                data: dose1Count
            },
            {
                type: 'bar',
                label: "2. d??vka",
                yAxisID: 'Dose',
                backgroundColor: BACKGROUND_CHART_COLORS.blue,
                borderColor: CHART_COLORS.blue,
                borderWidth: 2,
                data: dose2Count
            },
            {
                type: 'line',
                label: '1. d??vka kumulat??vne',
                yAxisID: 'Acc',
                borderColor: CHART_COLORS.red,
                pointRadius: 2,
                pointStyle: 'rectRot',
                data: dose1Sum
            },
            {
                type: 'line',
                label: '2. d??vka kumulat??vne',
                yAxisID: 'Acc',
                borderColor: CHART_COLORS.blue,
                pointRadius: 2,
                pointStyle: 'rectRot',
                data: dose2Sum
            }]
    };

    myChart = new Chart(document.getElementById("SlovakiaVaccinationsCanvas").getContext("2d"), {
        type: 'scatter',
        data: data,
        options: {
            plugins: {
                title: {
                    display: true,
                    text: "Vakcin??cia na Slovensku"
                },
                tooltip: {
                    callbacks: {
                        label: (tooltipItem, data) => (`${myChart.data.datasets[tooltipItem.datasetIndex].data[tooltipItem.dataIndex]}`)
                    }
                }
            },
            scales: {
                Dose: {
                    type: 'linear',
                    position: 'left',
                    title: {
                        display: true,
                        text: 'Po??et d??vok za de??',
                        font: {
                            size: 15
                        }
                    }
                },
                Acc: {
                    type: 'linear',
                    position: 'right',
                    title: {
                        display: true,
                        text: 'D??vky kumulat??vne',
                        font: {
                            size: 16
                        }
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