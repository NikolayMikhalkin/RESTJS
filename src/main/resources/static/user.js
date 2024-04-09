// User information
$(async function() {
    await getUserInfo();
});
async function getUserInfo() {
    fetch("http://localhost:8080/api/user/current")
        .then(response => response.json())
        .then(user => {
            let userTable = `$(
                <tr>
                    <td>${user.id}</td>
                    <td>${user.firstName}</td>
                    <td>${user.lastName}</td>
                    <td>${user.age}</td>
                    <td>${user.email}</td>
                    <td>${user.roles.map(role => " " + role.role.replace('ROLE_', ''))}</td>
                </tr>
            )`;
            $('#tBodyUserInformation').append(userTable);
        })
}