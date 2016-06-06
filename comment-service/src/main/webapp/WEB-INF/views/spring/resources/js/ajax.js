/**
 * Created by HSH on 16. 6. 5..
 */
var req = new XMLHttpRequest();
req.open('GET', 'http://localhost:8080/comments', true);
req.onreadystatechange = function (aEvt) {
    if (req.readyState == 4) {
        if(req.status == 200){
            document.getElementById('ajax').innerHTML = req.responseText;
            var data = JSON.parse(req.responseText);
            console.log(data);
            console.log(data.content[0].id);
            console.log(data.content[0].content);
            console.log(data.content[0].date);
            console.log(data.content[0].likeCount);
            console.log(data.content[0].dislikeCount);
            console.log(data.content[0].user.name);
        }
        else
            alert("Error loading page\n");
    }
};
req.send(null);