const studentsTable = new (function StudentsTable() {
    this.fetchStudents = function () {
        api
            .getAllStudents()
            .then(response => {
                if (typeof response.data === "undefined") return;
                this.clearStudentsTable();
                if (response.data.length > 0) {
                    response.data.map(el => {
                        this.addStudentToTable(el);
                    })
                } else {
                    this.createEmptyRow();
                }
            })
            .catch(error => {
                console.error(error);
            });

    }
    this.createEmptyRow = function () {
        $(".students-table").append(`<tr><th class="students-table__empty_row fade-in" colspan="7">Данных нет</th></tr>`);
    }
    this.clearStudentsTable = function () {
        let i = $(".students-table tbody tr").length - 1;
        while (i > 0) {
            $(".students-table tr")[i--].remove();
        }
    }
    this.addStudentToTable = function (el) {
        const self = this;
        if (typeof el.id === "undefined") return;
        if (typeof el.first_name === "undefined") return;
        if (typeof el.second_name === "undefined") return;
        if (typeof el.third_name === "undefined") return;
        if (typeof el.birthday_at === "undefined") return;
        if (typeof el.group_name === "undefined") return;
        const {id, first_name, second_name, third_name, birthday_at, group_name} = el;

        const studentRow = `
                <td>${id}</td>
                <td>${first_name}</td>
                <td>${second_name}</td>
                <td>${third_name}</td>
                <td>${birthday_at}</td>
                <td>${group_name}</td>
                <td><button class="students-table__action_button fade-in" id="el${id}" onClick="studentsTable.deleteStudentEvent(this.id)">Удалить</button></td>
            `;

        $(".students-table").append(`<tr>${studentRow}</tr>`);
    }
    this.checkLength = function (o, n, min, max) {
        if (o.val().length > max || o.val().length < min) {
            o.addClass("ui-state-error");
            this.updateTips("Длина " + n + " должна быть от " +
                min + " и до " + max);
            return false;
        } else {
            return true;
        }
    }

    this.checkRegexp = function (o, regexp, n) {
        if (!(regexp.test(o.val()))) {
            o.addClass("ui-state-error");
            this.updateTips("Недопустимый формат даты рождения, пример - " + n);
            return false;
        } else {
            return true;
        }
    }

    this.updateTips = function (t) {
        tips
            .text(t)
            .addClass("ui-state-highlight");
        setTimeout(function () {
            tips.removeClass("ui-state-highlight", 1500);
        }, 500);
    }
    this.addStudent = function () {
        let valid = true;
        allFields.removeClass("ui-state-error");

        const birthdayAtRegex = /^[0-9]{4}.[0-9]{2}.[0-9]{2}$/;

        valid = valid && this.checkLength(first_name, "имени", 1, 50);
        valid = valid && this.checkLength(second_name, "фамилии", 1, 50);
        valid = valid && this.checkLength(third_name, "отчества", 1, 50);
        valid = valid && this.checkLength(birthday_at, "дня рождения", 1, 50);
        valid = valid && this.checkRegexp(birthday_at, birthdayAtRegex, "1994.06.12");
        valid = valid && this.checkLength(group_name, "группы", 1, 50);

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
                    if ($(".students-table__empty_row")[0]) {
                        $(".students-table__empty_row")[0].remove();
                    }
                    this.addStudentToTable(response.data);
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
    this.deleteStudentEvent = function (el) {
        const id = el.match(/\d+/)[0];
        api.deleteStudent(id)
            .then(response => {
                this.fetchStudents();
            })
            .catch(error => {
                alert(error);
            })
            .finally(() => {
                dialog.dialog("close");
            })
    }
})();