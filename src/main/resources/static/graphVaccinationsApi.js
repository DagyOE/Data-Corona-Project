// indices for range selection
let minDate = createDate("2021-01-04");
let maxDate = createDate("2022-07-26");
let startDate = deconstructDateToUrl(createDate("2021-01-04"));
let endDate = deconstructDateToUrl(createDate("2022-07-26"));

let interval = 'daily';
let region = 1;

let myChart = new Chart(document.getElementById("SlovakiaVaccinationsCanvas").getContext("2d"), {});

//---------------------- LISTENERS

document.getElementById("SlovakiaVaccinationsInterval").addEventListener("change", event => {
    event.preventDefault();
    interval = event.target.value;
    fetchVaccinationData(`http://localhost:8080/api/vaccinations/in-slovakia/${interval}/${startDate}/${endDate}`,`http://localhost:8080/api/vaccinations/by-region/${interval}/${startDate}/${endDate}`);
});
document.getElementById("SlovakiaVaccinationsRegion").addEventListener("change", event => {
    event.preventDefault();
    region = event.target.value;
    fetchVaccinationData(`http://localhost:8080/api/vaccinations/in-slovakia/${interval}/${startDate}/${endDate}`,`http://localhost:8080/api/vaccinations/by-region/${interval}/${startDate}/${endDate}`);
})

//---------------------- CALLS

fetchVaccinationData(`http://localhost:8080/api/vaccinations/in-slovakia/${interval}/${startDate}/${endDate}`, `http://localhost:8080/api/vaccinations/by-region/${interval}/${startDate}/${endDate}`);
createDatePickerSlovakia();

//---------------------- FUNCTIONS

async function fetchVaccinationData(url, url2) {
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
            initializeArrays(dose1Count);
            initializeArrays(dose2Count);
            initializeArrays(dose1Sum);
            initializeArrays(dose2Sum);
            // filling memory
            const dataLength = data.length;
            for (let i = 0; i < dataLength; i++) {
                dose1Count[0][i] = data[i].dose1Count;
                dose2Count[0][i] = data[i].dose2Count;
                dose1Sum[0][i] = data[i].dose1Sum;
                dose2Sum[0][i] = data[i].dose2Sum;
                publishedOn[i] = data[i].publishedOn;
            }

            fetchRegionVaccinationData(url2, dose1Count, dose2Count, dose1Sum, dose2Sum, publishedOn);
        })
}

async function fetchRegionVaccinationData(url, dose1Count, dose2Count, dose1Sum, dose2Sum, publishedOn) {
    await fetch(url)
        .then(response => {
            if (response.ok) {
                return response.json();
            } else {
                return Promise.reject(new Error(`Region vaccination data acquisition failed. Server answered with ${response.status}: ${response.statusText}.`));
            }
        })
        .then(data => {
            publishedOnTmp = [];
            // filling memory
            const dataLength = data.length;
            minDate = createDate(data[dataLength - 1].publishedOn);
            for (let i = 0; i < dataLength; i++) {
                let index = Math.round((createDate(data[i].publishedOn).getTime() - minDate.getTime()) / millisecondsInDay);
                let region = data[i].region.id;

                dose1Count[region][index] = data[i].dose1Count;
                dose2Count[region][index] = data[i].dose2Count;
                dose1Sum[region][index] = data[i].dose1Sum;
                dose2Sum[region][index] = data[i].dose2Sum;
                publishedOnTmp[index] = data[i].publishedOn;
                // publishedOn[index] = data[i].publishedOn;
            }
            fillArraysWithPreviousDataPoint(dose1Sum, dose2Sum, publishedOnTmp);
            myChart.destroy();
            plotSlovakiaVaccinations(dose1Count[region], dose2Count[region], dose1Sum[region], dose2Sum[region], publishedOnTmp);
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
            startDate: minDate,
            endDate: maxDate
        }, function(start, end, label) {
            startDate = deconstructDateToUrl(start._d);
            endDate = deconstructDateToUrl(end._d);
            fetchVaccinationData(`http://localhost:8080/api/vaccinations/in-slovakia/${interval}/${startDate}/${endDate}`,`http://localhost:8080/api/vaccinations/by-region/${interval}/${startDate}/${endDate}`);
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
                label: "1. dávka",
                yAxisID: 'Dose',
                backgroundColor: BACKGROUND_CHART_COLORS.red,
                borderColor: CHART_COLORS.red,
                borderWidth: 2,
                data: dose1Count
            },
            {
                type: 'bar',
                label: "2. dávka",
                yAxisID: 'Dose',
                backgroundColor: BACKGROUND_CHART_COLORS.blue,
                borderColor: CHART_COLORS.blue,
                borderWidth: 2,
                data: dose2Count
            },
            {
                type: 'line',
                label: '1. dávka kumulatívne',
                yAxisID: 'Acc',
                borderColor: CHART_COLORS.red,
                pointRadius: 2,
                pointStyle: 'rectRot',
                data: dose1Sum
            },
            {
                type: 'line',
                label: '2. dávka kumulatívne',
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
                    text: "Vakcinácia na Slovensku"
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
                        text: 'Počet dávok za deň',
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
                        text: 'Dávky kumulatívne',
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