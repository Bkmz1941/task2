function updateTips(t) {
    tips
        .text(t)
        .addClass("ui-state-highlight");
    setTimeout(function () {
        tips.removeClass("ui-state-highlight", 1500);
    }, 500);
}

function createUser() {
    let valid = true;
    allFields.removeClass("ui-state-error");

    valid = valid && checkLength(first_name, "имени", 1, 50);
    valid = valid && checkLength(second_name, "фамилии", 1, 50);
    valid = valid && checkLength(third_name, "отчества", 1, 50);
    valid = valid && checkLength(birthday_at, "дня рождения", 1, 50);
    valid = valid && checkLength(group_name, "группы", 1, 50);

    const payload = {
        first_name: first_name.val(),
        second_name: second_name.val(),
        third_name: third_name.val(),
        birthday_at: birthday_at.val(),
        group_name: group_name.val()
    };

    if (valid) {
        api.postAddStudent(payload)
            .then(response => {
                if (typeof response.data === "undefined") return;
                addStudentToTable(response.data);
            })
            .catch(error => {
                alert(error);
            })
            .finally(() => {
                dialog.dialog("close");
            })
    }
    return valid;
}


dialog = $("#dialog").dialog({
    autoOpen: false,
    height: 520,
    width: 450,
    buttons: {
        "Добавить": createUser,
        "Отмена": function () {
            dialog.dialog("close");
        }
    },
});

form = dialog.find("form").on("submit", function (event) {
    event.preventDefault();
    createUser();
});

$(".students-table__button").on("click", function () {
    $("#dialog").dialog("open");
});