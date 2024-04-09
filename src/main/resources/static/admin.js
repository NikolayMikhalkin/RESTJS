
$(async function () {
    await allUsers();
    editUser();
    deleteUser();
    await newUser();
});

const userFetchService = {
    head: {
        'Accept': 'application/json',
        'Content-Type': 'application/json',
        'Referer': null
    },
    // bodyAdd : async function(user) {return {'method': 'POST', 'headers': this.head, 'body': user}},
    findAllUsers: async () => await fetch('api/admin'),
    findOneUser: async (id) => await fetch(`api/admin/users/${id}`),
    addNewUser: async (user) => await fetch('api/admin/users', {method: 'POST', headers: userFetchService.head, body: JSON.stringify(user)}),
    updateUser: async (user, id) => await fetch(`api/admin/users/${id}`, {method: 'PUT', headers: userFetchService.head, body: JSON.stringify(user)}),
    deleteUser: async (id) => await fetch(`api/admin/users/${id}`, {method: 'DELETE', headers: userFetchService.head})
}

async function getUser(id) {
    let url = "http://localhost:8080/api/admin/user/" + id;
    let response = await fetch(url);
    return await response.json();
}

// UsersTable

const tbody = $('#tableBodyAdmin');
async function allUsers() {
    tbody.empty()
    await userFetchService.findAllUsers()
        .then(res => res.json())
        .then(data => {
            data.forEach(user => {
                let usersTable = `$(
                        <tr>
                            <td>${user.id}</td>
                            <td>${user.firstName}</td>
                            <td>${user.lastName}</td>
                            <td>${user.age}</td>
                            <td>${user.email}</td>
                            <td>${user.roles.map(role => " " + role.role.replace('ROLE_', ''))}</td>
                            <td>
                                <button type="button" class="btn btn-info" data-bs-toggle="modal" id="editButton"
                                data-action="editModal"  data-id="${user.id}" data-bs-target="#editModal">Edit</button>
                            </td>
                            <td>
                                <button type="button" class="btn btn-danger" data-bs-toggle="modal" id="deleteButton"
                                data-action="deleteModal" data-id="${user.id}" data-bs-target="#deleteModal">Delete</button>
                            </td>
                        </tr>)`;
                tbody.append(usersTable);
            })
        })
}

// Edit

function editUser() {
    const editForm = document.forms["editUserForm"];

    editForm.addEventListener("submit", ev => {
        ev.preventDefault();
        let editUserRoles = [];
        for (let i = 0; i < editForm.roles.options.length; i++) {
            if (editForm.roles.options[i].selected) {
                editUserRoles.push({
                    id: editForm.roles.options[i].value,
                    role: editForm.roles.options[i].text
                })
            }
        }

        fetch("http://localhost:8080/api/admin/user/" + editForm.id.value, {
            method: 'PATCH',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({
                id: editForm.id.value,
                firstName: editForm.firstName.value,
                lastName: editForm.lastName.value,
                age: editForm.age.value,
                email: editForm.email.value,
                password: editForm.password.value,
                roles: editUserRoles
            })
        }).then(() => {
            $('#buttonClose').click();
            allUsers();
        })
    })
}

$('#editModal').on('show.bs.modal', ev => {
    let button = $(ev.relatedTarget);
    let id = button.data('id');
    showEditModal(id);
})

async function showEditModal(id) {
    $('#rolesEdit').empty();
    let user = await getUser(id);
    let form = document.forms["editUserForm"];
    form.id.value = user.id;
    form.name.value = user.firstName;
    form.lastName.value = user.lastName;
    form.age.value = user.age;
    form.username.value = user.email;
    form.password.value = user.password;

    await fetch("http://localhost:8080/api/admin/roles")
        .then(res => res.json())
        .then(roles => {
            roles.forEach(role => {
                let selectedRole = false;
                for (let i = 0; i < user.roles.length; i++) {
                    if (user.roles[i].role === role.role) {
                        selectedRole = true;
                        break;
                    }
                }
                let el = document.createElement("option");
                el.text = role.role.replace('ROLE_', '');
                el.value = role.id;
                if (selectedRole) el.selected = true;
                $('#rolesEdit')[0].appendChild(el);
            })
        })
}

// Delete

function deleteUser() {
    const deleteForm = document.forms["deleteUserForm"];

    deleteForm.addEventListener("submit", ev => {
        ev.preventDefault();

        fetch("http://localhost:8080/api/admin/user/" + deleteForm.id.value, {
            method: 'DELETE',
            headers: {
                'Content-Type': 'application/json'
            }
        }).then(() => {
            $('#buttonCloseDelete').click();
            allUsers();
        })
    })
}

$('#deleteModal').on('show.bs.modal', ev => {
    let button = $(ev.relatedTarget);
    let id = button.data('id');
    showDeleteModal(id);
})

async function showDeleteModal(id) {
    $('#deleteRoles').empty();
    let user = await getUser(id);
    let form = document.forms["deleteUserForm"];

    form.id.value = user.id;
    form.name.value = user.firstName;
    form.lastName.value = user.lastName;
    form.age.value = user.age;
    form.username.value = user.email;

    await fetch("http://localhost:8080/api/admin/roles")
        .then(res => res.json())
        .then(roles => {
            roles.forEach(role => {
                let selectedRole = false;
                for (let i = 0; i < user.roles.length; i++) {
                    if (user.roles[i].role === role.role) {
                        selectedRole = true;
                        break;
                    }
                }
                let el = document.createElement("option");
                el.text = role.role;
                el.value = role.id;
                if (selectedRole) el.selected = true;
                $('#rolesDelete')[0].appendChild(el);
            })
        })
}

// New user

async function newUser() {
    await fetch("http://localhost:8080/api/admin/roles")
        .then(res => res.json())
        .then(roles => {
            roles.forEach(role => {
                let el = document.createElement("option");
                el.text = role.role.replace('ROLE_', '');
                el.value = role.id;
                $('#rolesAdd')[0].appendChild(el);
            })
        })

    const form = document.forms["newUserForm"];
    form.addEventListener('submit', addNewUser)

    function addNewUser(e) {
        e.preventDefault();
        let addRoles = [];
        for (let i = 0; i < form.roles.options.length; i++) {
            if (form.roles.options[i].selected) addRoles.push({
                id : form.roles.options[i].value,
                role : form.roles.options[i].text
            })
        }
        fetch("http://localhost:8080/api/admin/new", {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({
                firstName: form.name.value,
                lastName: form.lastName.value,
                age: form.age.value,
                email: form.username.value,
                password: form.password.value,
                roles: addRoles
            })
        }).then(() => {
            form.reset();
            allUsers();
            $('#home-tab').click();
        })
    }
}