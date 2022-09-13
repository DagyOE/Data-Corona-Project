// memory for data
let dose1Count = [];
let dose2Count = [];
let dose1Sum = [];
let dose2Sum = [];
let updatedAt = [];
let publishedOn = [];
let dose1CountWeek = [];
let dose2CountWeek = [];
let dose1SumWeek = [];
let dose2SumWeek = [];
let updatedAtWeek = [];
let publishedOnWeek = [];
let dose1CountMonth = [];
let dose2CountMonth = [];
let dose1SumMonth = [];
let dose2SumMonth = [];
let updatedAtMonth = [];
let publishedOnMonth = [];
let publishedOnMonthXAxis = [];

// indices for range selection
let minDate = 0;
let maxDate = 0;
let startIndexSlovakia = 0;
let endIndexSlovakia = 0;

let interval = 'weekly';
let region = 0;

let myChart = new Chart(document.getElementById("SlovakiaVaccinationsCanvas").getContext("2d"), {});

//---------------------- LISTENERS

document.getElementById("SlovakiaVaccinationsInterval").addEventListener("change", event => {
    event.preventDefault();
    interval = event.target.value;
    myChart.destroy();
    selectDataAndPlotSlovakiaVaccinations(interval);
});
document.getElementById("SlovakiaVaccinationsRegion").addEventListener("change", event => {
    event.preventDefault();
    region = event.target.value;
    myChart.destroy();
    selectDataAndPlotSlovakiaVaccinations(interval);
})

//---------------------- CALLS

initializeArrays(dose1Count);
initializeArrays(dose2Count);
initializeArrays(dose1Sum);
initializeArrays(dose2Sum);
initializeArrays(dose1CountWeek);
initializeArrays(dose2CountWeek);
initializeArrays(dose1SumWeek);
initializeArrays(dose2SumWeek);
initializeArrays(dose1CountMonth);
initializeArrays(dose2CountMonth);
initializeArrays(dose1SumMonth);
initializeArrays(dose2SumMonth);

fetchVaccinationData("http://localhost:8080/api/vaccinations/in-slovakia");
fetchRegionVaccinationData("http://localhost:8080/api/vaccinations/by-region");

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
            // filling memory
            const dataLength = data.length;
            for (let i = 0; i < dataLength; i++) {
                dose1Count[0][i] = data[i].dose1Count;
                dose2Count[0][i] = data[i].dose2Count;
                dose1Sum[0][i] = data[i].dose1Sum;
                dose2Sum[0][i] = data[i].dose2Sum;
                updatedAt[i] = data[i].updatedAt;
                publishedOn[i] = data[i].publishedOn;
            }
            dose1Count[0].reverse();
            dose2Count[0].reverse();
            dose1Sum[0].reverse();
            dose2Sum[0].reverse();
            updatedAt.reverse();
            publishedOn.reverse();
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
            minDate = createDate(data[0].publishedOn);
//            minDate = createDate(publishedOn[0]);
//            maxDate = createDate(publishedOn[publishedOn.length - 1]);
            maxDate = createDate(data[data.length - 1].publishedOn);

            // filling memory
            const dataLength = data.length;
            for (let i = 0; i < dataLength; i++) {
                let index = Math.round((createDate(data[i].publishedOn).getTime() - minDate.getTime()) / millisecondsInDay);
                let region = data[i].region.id;

                dose1Count[region][index] = data[i].dose1Count;
                dose2Count[region][index] = data[i].dose2Count;
                dose1Sum[region][index] = data[i].dose1Sum;
                dose2Sum[region][index] = data[i].dose2Sum;
            }
            for (let region = 1; region < 9; region++) {
                dose1Count[region].reverse();
                dose2Count[region].reverse();
                // dose1Sum[region].reverse();
                // dose2Sum[region].reverse();
            }
            fillArraysWithPreviousDataPoint();
            prepareWeeklyData();
            prepareMonthlyData();

            // plotSlovakiaVaccinations(dose1Count, dose2Count, dose1Sum, dose2Sum, publishedOn);
            startIndexSlovakia = 0;
            endIndexSlovakia = publishedOn.length - 1;
            myChart.destroy();
            selectDataAndPlotSlovakiaVaccinations(interval);
            createDatePickerSlovakia();
        })
}

function fillArraysWithPreviousDataPoint() {
    const length = publishedOn.length;
    for (let region = 1; region < 9; region++) {
        for (let idx = 1; idx < length; idx++) {
            if (dose1Count[region][idx] === undefined) {
                dose1Count[region][idx] = 0;
            }
            if (dose2Count[region][idx] === undefined) {
                dose2Count[region][idx] = 0;
            }
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
            startIndexSlovakia = Math.round((start._d.getTime() - minDate.getTime()) / millisecondsInDay);
            endIndexSlovakia = Math.round((end._d.getTime() - minDate.getTime()) / millisecondsInDay);
            myChart.destroy();
            selectDataAndPlotSlovakiaVaccinations(interval);
        });
    });
}

function prepareWeeklyData() {
    for (let region = 0; region < 9; region++) {
        let dayIdx = 0;
        let weekIdx = 0;
        while (dayIdx < publishedOn.length) {
            dose1CountWeek[region][weekIdx] = 0;
            dose2CountWeek[region][weekIdx] = 0;
            do {
                dose1CountWeek[region][weekIdx] += dose1Count[region][dayIdx];
                dose2CountWeek[region][weekIdx] += dose2Count[region][dayIdx];
                dayIdx++;
            } while (dayIdx < publishedOn.length && createDate(publishedOn[dayIdx]).getDay() != 0);
            publishedOnWeek[weekIdx] = publishedOn[dayIdx - 1];
            dose1SumWeek[region][weekIdx] = dose1Sum[region][dayIdx - 1];
            dose2SumWeek[region][weekIdx] = dose2Sum[region][dayIdx - 1];
            weekIdx++;
        }
    }
}

function prepareMonthlyData() {
    for (let region = 0; region < 9; region++) {
        let dayIdx = 0;
        let monthIdx = 0;
        while(dayIdx < publishedOn.length) {
            dose1CountMonth[region][monthIdx] = 0;
            dose2CountMonth[region][monthIdx] = 0;
            do {
                dose1CountMonth[region][monthIdx] += dose1Count[region][dayIdx];
                dose2CountMonth[region][monthIdx] += dose2Count[region][dayIdx];
                dayIdx++;
            } while(dayIdx < publishedOn.length && createDate(publishedOn[dayIdx]).getDate() != 1);
            publishedOnMonth[monthIdx] = publishedOn[dayIdx - 1];
            publishedOnMonthXAxis[monthIdx] = month[createDate(publishedOn[dayIdx - 1]).getMonth()] + ' ' + publishedOn[dayIdx - 1].substring(0, 4);
            dose1SumMonth[region][monthIdx] = dose1Sum[region][dayIdx - 1];
            dose2SumMonth[region][monthIdx] = dose2Sum[region][dayIdx - 1];

            monthIdx++;
        }
    }
}

function selectDataAndPlotSlovakiaVaccinations(interval) {
    let dose1CountNew = [];
    let dose2CountNew = [];
    let dose1SumNew = [];
    let dose2SumNew = [];
    let publishedOnNew = [];

    if (interval === 'daily') {
        for (let i = startIndexSlovakia; i < endIndexSlovakia; i++) {
            dose1CountNew[i - startIndexSlovakia] = dose1Count[region][i];
            dose2CountNew[i - startIndexSlovakia] = dose2Count[region][i];
            dose1SumNew[i - startIndexSlovakia] = dose1Sum[region][i];
            dose2SumNew[i - startIndexSlovakia] = dose2Sum[region][i];
            publishedOnNew[i - startIndexSlovakia] = publishedOn[i];
        }
    } else if (interval === 'weekly') {
        let startDate = createDate(publishedOn[startIndexSlovakia]);
        let endDate = createDate(publishedOn[endIndexSlovakia]);

        let startIdx = -1;
        let endIdx = -1;
        for (let i = 0; i < publishedOnWeek.length; i++) {
            let curDate = createDate(publishedOnWeek[i]);
            if (startIdx === -1 && curDate >= startDate) {
                startIdx = i;
            }
            if (endIdx === -1 && curDate >= endDate) {
                endIdx = i;
                break;
            }
        }
        for (let i = startIdx; i < endIdx; i++) {
            dose1CountNew[i - startIdx] = dose1CountWeek[region][i];
            dose2CountNew[i - startIdx] = dose2CountWeek[region][i];
            dose1SumNew[i - startIdx] = dose1SumWeek[region][i];
            dose2SumNew[i - startIdx] = dose2SumWeek[region][i];
            publishedOnNew[i - startIdx] = publishedOnWeek[i];
        }
    } else if (interval === 'monthly') {
        let startDate = createDate(publishedOn[startIndexSlovakia]);
        let endDate = createDate(publishedOn[endIndexSlovakia]);

        let startIdx = -1;
        let endIdx = -1;
        for (let i = 0; i < publishedOnMonth.length; i++) {
            let curDate = createDate(publishedOnMonth[i]);
            if (startIdx === -1 && curDate >= startDate) {
                startIdx = i;
            }
            if (endIdx === -1 && curDate >= endDate) {
                endIdx = i;
                break;
            }
        }
        for (let i = startIdx; i < endIdx; i++) {
            dose1CountNew[i - startIdx] = dose1CountMonth[region][i];
            dose2CountNew[i - startIdx] = dose2CountMonth[region][i];
            dose1SumNew[i - startIdx] = dose1SumMonth[region][i];
            dose2SumNew[i - startIdx] = dose2SumMonth[region][i];
            publishedOnNew[i - startIdx] = publishedOnMonthXAxis[i];
        }
    }

    plotSlovakiaVaccinations(dose1CountNew, dose2CountNew, dose1SumNew, dose2SumNew, publishedOnNew);
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