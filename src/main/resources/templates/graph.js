    const CHART_COLORS = {
    red: 'rgb(255, 99, 132)',
    orange: 'rgb(255, 159, 64)',
    yellow: 'rgb(255, 205, 86)',
    green: 'rgb(75, 192, 192)',
    blue: 'rgb(54, 162, 235)',
    purple: 'rgb(153, 102, 255)',
    grey: 'rgb(201, 203, 207)'
};

    const BACKGROUND_CHART_COLORS = {
    red: 'rgba(255, 99, 132, 0.5)',
    orange: 'rgba(255, 159, 64, 0.5)',
    yellow: 'rgba(255, 205, 86, 0.5)',
    green: 'rgba(75, 192, 192, 0.5)',
    blue: 'rgba(54, 162, 235, 0.5)',
    purple: 'rgba(153, 102, 255, 0.5)',
    grey: 'rgba(201, 203, 207, 0.5)'
}

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
    let startIndexSlovakia = 0;
    let endIndexSlovakia = 0;

    const month = ["January","February","March","April","May","June","July","August","September","October","November","December"];
    const millisecondsInDay = 1000 * 3600 * 24;
    let interval = 'weekly';

    var myChart = new Chart(document.getElementById("SlovakiaVaccinations").getContext("2d"), {});

    document.getElementById("SlovakiaVaccinationsInterval").addEventListener("change", event => {
    event.preventDefault();
    interval = event.target.value;
    myChart.destroy();
    selectDataAndPlotSlovakiaVaccinations(interval);
});

    fetchVaccinationData("http://localhost:8080/api/vaccinations/in-slovakia");
    // selectDataAndPlotSlovakiaVaccinations();

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
                dose1Count[i] = data[i].dose1Count;
                dose2Count[i] = data[i].dose2Count;
                dose1Sum[i] = data[i].dose1Sum;
                dose2Sum[i] = data[i].dose2Sum;
                updatedAt[i] = data[i].updatedAt;
                publishedOn[i] = data[i].publishedOn;
            }
            dose1Count.reverse();
            dose2Count.reverse();
            dose1Sum.reverse();
            dose2Sum.reverse();
            updatedAt.reverse();
            publishedOn.reverse();
            prepareWeeklyData();
            prepareMonthlyData();

            myChart.destroy();
            // plotSlovakiaVaccinations(dose1Count, dose2Count, dose1Sum, dose2Sum, publishedOn);
            startIndexSlovakia = 0;
            endIndexSlovakia = publishedOn.length - 1;
            selectDataAndPlotSlovakiaVaccinations(interval);
            createDatePickerSlovakia();
        })
}

    function createDatePickerSlovakia() {
    let minDate = createDate(publishedOn[0]);
    let maxDate = createDate(publishedOn[publishedOn.length - 1]);
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

    function createDate(date) {
    return new Date(parseInt(date.substring(0, 4)), parseInt(date.substring(5, 7)) - 1, parseInt(date.substring(8, 10)));
}

    function prepareWeeklyData() {
    let dayIdx = 0;
    let weekIdx = 0;
    while(dayIdx < publishedOn.length) {
    dose1CountWeek[weekIdx] = 0;
    dose2CountWeek[weekIdx] = 0;
    do {
    dose1CountWeek[weekIdx] += dose1Count[dayIdx];
    dose2CountWeek[weekIdx] += dose2Count[dayIdx];
    dayIdx++;
} while(dayIdx < publishedOn.length && createDate(publishedOn[dayIdx]).getDay() != 0);
    // updatedAtWeek[weekIdx] = updatedAt[dayIdx - 1];
    publishedOnWeek[weekIdx] = publishedOn[dayIdx - 1];
    dose1SumWeek[weekIdx] = dose1Sum[dayIdx - 1];
    dose2SumWeek[weekIdx] = dose2Sum[dayIdx - 1];
    weekIdx++;
}
}

    function prepareMonthlyData() {
    let dayIdx = 0;
    let monthIdx = 0;
    while(dayIdx < publishedOn.length) {
    dose1CountMonth[monthIdx] = 0;
    dose2CountMonth[monthIdx] = 0;
    do {
    dose1CountMonth[monthIdx] += dose1Count[dayIdx];
    dose2CountMonth[monthIdx] += dose2Count[dayIdx];
    dayIdx++;
} while(dayIdx < publishedOn.length && createDate(publishedOn[dayIdx]).getDate() != 1);
    // updatedAtMonth[weekIdx] = updatedAt[dayIdx - 1];
    publishedOnMonth[monthIdx] = publishedOn[dayIdx - 1];
    publishedOnMonthXAxis[monthIdx] = month[createDate(publishedOn[dayIdx - 1]).getMonth()] + ' ' + publishedOn[dayIdx - 1].substring(0, 4);
    dose1SumMonth[monthIdx] = dose1Sum[dayIdx - 1];
    dose2SumMonth[monthIdx] = dose2Sum[dayIdx - 1];

    monthIdx++;
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
    dose1CountNew[i - startIndexSlovakia] = dose1Count[i];
    dose2CountNew[i - startIndexSlovakia] = dose2Count[i];
    dose1SumNew[i - startIndexSlovakia] = dose1Sum[i];
    dose2SumNew[i - startIndexSlovakia] = dose2Sum[i];
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
    dose1CountNew[i - startIdx] = dose1CountWeek[i];
    dose2CountNew[i - startIdx] = dose2CountWeek[i];
    dose1SumNew[i - startIdx] = dose1SumWeek[i];
    dose2SumNew[i - startIdx] = dose2SumWeek[i];
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
    dose1CountNew[i - startIdx] = dose1CountMonth[i];
    dose2CountNew[i - startIdx] = dose2CountMonth[i];
    dose1SumNew[i - startIdx] = dose1SumMonth[i];
    dose2SumNew[i - startIdx] = dose2SumMonth[i];
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
    label: "Dose 1",
    yAxisID: 'Dose',
    backgroundColor: BACKGROUND_CHART_COLORS.red,
    borderColor: CHART_COLORS.red,
    borderWidth: 2,
    data: dose1Count
},
{
    type: 'bar',
    label: "Dose 2",
    yAxisID: 'Dose',
    backgroundColor: BACKGROUND_CHART_COLORS.blue,
    borderColor: CHART_COLORS.blue,
    borderWidth: 2,
    data: dose2Count
},
{
    type: 'line',
    label: 'Dose 1 accumulation',
    yAxisID: 'Acc',
    borderColor: CHART_COLORS.red,
    pointRadius: 2,
    pointStyle: 'rectRot',
    data: dose1Sum
},
{
    type: 'line',
    label: 'Dose 2 accumulation',
    yAxisID: 'Acc',
    borderColor: CHART_COLORS.blue,
    pointRadius: 2,
    pointStyle: 'rectRot',
    data: dose2Sum
}]
};

    myChart = new Chart(document.getElementById("SlovakiaVaccinations").getContext("2d"), {
    type: 'scatter',
    data: data,
    options: {
    plugins: {
    title: {
    display: true,
    text: "Vaccination in Slovakia"
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
    position: 'left'
},
    Acc: {
    type: 'linear',
    position: 'right'
}
},
    hover: {
    mode: 'point',
},
    responsive: true
}
});
}