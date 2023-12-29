const API_URL = "http://localhost:8000/api";

function Api() {
    this.getAllStudents = function () {
        return new Promise((resolve, reject) => {
            fetch(`${API_URL}/students`)
                .then(response => response.json())
                .then(data => {
                    resolve(data);
                })
                .catch(error => {
                    reject(error);
                })
        })
    }
    this.postAddStudent = function (payload) {
        return new Promise((resolve, reject) => {
            fetch(`${API_URL}/students`,{
                method: 'POST',
                body: JSON.stringify(payload)
            })
                .then(response => response.json())
                .then(data => {
                    resolve(data);
                })
                .catch(error => {
                    reject(error);
                })
        })
    }
    this.deleteStudent = function (id) {
        return new Promise((resolve, reject) => {
            fetch(`${API_URL}/students/${id}`,{
                method: 'DELETE',
            })
                .then(response => response.json())
                .then(data => {
                    resolve(data);
                })
                .catch(error => {
                    reject(error);
                })
        })
    }
}