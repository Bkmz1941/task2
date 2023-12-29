const api = new Api();
let dialog, form;
let tips = $(".validateTips");
let first_name = $("#first_name");
let second_name = $("#second_name");
let third_name = $("#third_name");
let birthday_at = $("#birthday_at");
let group_name = $("#group_name");
let allFields = $([]).add(first_name).add(second_name).add(third_name).add(birthday_at).add(group_name);

studentsTable.fetchStudents();


dialog = $("#dialog").dialog({
    autoOpen: false,
    height: 550,
    width: 650,
    buttons: {
        "Добавить": studentsTable.addStudent.bind(studentsTable),
        "Отмена": function () {
            dialog.dialog("close");
        }
    },
    close: function () {
        form[0].reset();
        allFields.removeClass("ui-state-error");
    }
});

form = dialog.find("form").on("submit", function (event) {
    event.preventDefault();
    studentsTable.addStudent();
});
$(".students-table__button").on("click", function () {
    $("#dialog").dialog("open");
});



