const mealAjaxUrl = "profile/meals/";

// https://stackoverflow.com/a/5064235/548473
const ctx = {
    ajaxUrl: mealAjaxUrl,
    updateTable: function () {
        $.ajax({
            type: "GET",
            url: mealAjaxUrl + "filter",
            data: $("#filter").serialize()
        }).done(updateTableByData);
    }
}

function clearFilter() {
    $("#filter")[0].reset();
    $.get(mealAjaxUrl, updateTableByData);
}

function dateFormat(date) {
    return date.replace("T", " ");
}

$(function () {
    makeEditable(
        $("#datatable").DataTable({
            "ajax": {
                "url": mealAjaxUrl,
                "dataSrc": ""
            },
            "paging": false,
            "info": true,
            "columns": [
                {
                    "data": "dateTime",
                    "render": function (date, type, row) {
                        if (type === "display") {
                            return dateFormat(date);
                        }
                        return date;
                    }
                },
                {
                    "data": "description"
                },
                {
                    "data": "calories"
                },
                {
                    "orderable": false,
                    "defaultContent": "",
                    "render": renderEditBtn
                },
                {
                    "orderable": false,
                    "defaultContent": "",
                    "render": renderDeleteBtn
                }
            ],
            "order": [
                [
                    0,
                    "desc"
                ]
            ],
            "createdRow": function (row, data, dataIndex) {
                $(row).attr("data-meal-excess", !!data.excess);
            }
        })
    )
});

$("#startDate").datetimepicker({
    timepicker: false,
    format: "Y-m-d",
    onShow: function (ct) {
        let endDate = $("#endDate").val();
        this.setOptions({
            maxDate: endDate ? endDate : false
        })
    }
});

$("#endDate").datetimepicker({
    timepicker: false,
    format: "Y-m-d",
    onShow: function (ct) {
        let startDate = $("#startDate").val();
        this.setOptions({
            minDate: startDate ? startDate : false
        })
    }
});

$("#startTime").datetimepicker({
    datepicker: false,
    format: "H:i",
    onShow: function (ct) {
        let endTime = $("#endTime").val();
        this.setOptions({
            maxTime: endTime ? endTime : false
        })
    }
});

$("#endTime").datetimepicker({
    datepicker: false,
    format: "H:i",
    onShow: function (ct) {
        let startTime = $("#startTime").val();
        this.setOptions({
            minTime: startTime ? startTime : false
        })
    }
});

$("#dateTime").datetimepicker({
    format: "Y-m-d H:i"
})