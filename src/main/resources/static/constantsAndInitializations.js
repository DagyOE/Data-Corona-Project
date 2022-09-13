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


function initializeArrays(arr) {
    for (let i = 0; i < 9; i++) {
        arr[i] = [];
    }
}

function createDate(date) {
    return new Date(parseInt(date.substring(0, 4)), parseInt(date.substring(5, 7)) - 1, parseInt(date.substring(8, 10)));
}