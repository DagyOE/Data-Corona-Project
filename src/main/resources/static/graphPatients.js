// constants
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

const month = ["Január","Február","Marec","Apríl","Máj","Jún","Júl","August","September","Október","November","December"];
const millisecondsInDay = 1000 * 3600 * 24;

// memory for data
let confirmedCovid = [];
let nonCovid = [];
let suspectedCovid = [];
let ventilatedCovid = [];
let publishedOn = [];
let confirmedCovidWeek = [];
let nonCovidWeek = [];
let suspectedCovidSumWeek = [];
let ventilatedCovidSumWeek = [];
let publishedOnWeek = [];
let confirmedCovidMonth = [];
let nonCovidMonth = [];
let suspectedCovidMonth = [];
let ventilatedCovidMonth = [];
let publishedOnMonth = [];
let publishedOnMonthXAxis = [];

// indices for range selection
let minDate = 0;
let maxDate = 0;
let startIndexSlovakia = 0;
let endIndexSlovakia = 0;

let interval = 'weekly';
let region = 1;

var myChart = new Chart(document.getElementById("Patients").getContext("2d"), {});

//---------------------- LISTENERS

document.getElementById("PatientsInterval").addEventListener("change", event => {
    event.preventDefault();
    interval = event.target.value;
    myChart.destroy();
    selectDataAndPlotSlovakiaVaccinations(interval);
});
document.getElementById("PatientsRegion").addEventListener("change", event => {
    event.preventDefault();
    region = event.target.value;
    myChart.destroy();
    selectDataAndPlotSlovakiaVaccinations(interval);
})

//---------------------- CALLS

initializeArrays(confirmedCovid);
initializeArrays(nonCovid);
initializeArrays(suspectedCovid);
initializeArrays(ventilatedCovid);
initializeArrays(confirmedCovidWeek);
initializeArrays(nonCovidWeek);
initializeArrays(suspectedCovidSumWeek);
initializeArrays(ventilatedCovidSumWeek);
initializeArrays(confirmedCovidMonth);
initializeArrays(nonCovidMonth);
initializeArrays(suspectedCovidMonth);
initializeArrays(ventilatedCovidMonth);
initializeArrays2D();


//---------------------- FUNCTIONS

// function initializeArrays(arr) {
//     for (let i = 0; i < 9; i++) {
//         arr[i] = [];
//     }
// }
//
// function initializeArrays2D() {
//     for (let region = 1; region < 9; region++) {
//         dose1Count[region][0] = 0;
//         dose2Count[region][0] = 0;
//         dose1Sum[region][0] = 0;
//         dose2Sum[region][0] = 0;
//     }
// }

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
            const dataLength = data.length;
            for (let i = 0; i < dataLength; i++) {
                
            }
        })
}