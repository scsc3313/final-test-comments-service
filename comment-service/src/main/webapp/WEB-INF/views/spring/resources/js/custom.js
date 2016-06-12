/**
 * Created by HSH on 16. 6. 5..
 */
var req = new XMLHttpRequest();
var sessionId = document.getElementsByClassName('session')[0].getAttribute('value');
req.open('GET', 'http://localhost:8080/comments', true);
req.onreadystatechange = function () {
    if (req.readyState == 4) {
        if (req.status == 200) {

            var contents = document.getElementsByClassName('contents');
            var data = JSON.parse(req.responseText);

            for (i = 0; i < data.content.length; i++) {

                var contentId = data.content[i].id;

                var contentTr = document.createElement('tr');
                contents[0].appendChild(contentTr);

                var imgTd = document.createElement('td');
                var img = document.createElement("img");

                var nameTd = document.createElement('td');
                var contentTd = document.createElement('td');

                var evalTd = document.createElement('td');
                var likeCountBtn = document.createElement('button');
                var dislikeCountBtn = document.createElement('button');

                var likeCountA = document.createElement('a');
                var dislikeCountA = document.createElement('a');

                var dateTd = document.createElement('td');

                img.setAttribute('src', 'resources/images/' + data.content[i].user.userProfileImage);
                img.setAttribute('width', '35');
                img.setAttribute('height', '35');
                imgTd.appendChild(img);

                nameTd.innerHTML = data.content[i].user.name;
                contentTd.innerHTML = data.content[i].content;

                likeCountBtn.setAttribute('type', 'button');
                likeCountA.setAttribute('href', '/like/'+contentId);
                likeCountA.innerHTML = '추천(' + data.content[i].likeCount + ')';
                likeCountBtn.appendChild(likeCountA);

                dislikeCountBtn.setAttribute('type', 'button');
                dislikeCountA.setAttribute('href', '/dislike/'+contentId);
                dislikeCountA.innerHTML = '반대(' + data.content[i].dislikeCount + ')';
                dislikeCountBtn.appendChild(dislikeCountA);

                evalTd.appendChild(likeCountBtn);
                evalTd.appendChild(dislikeCountBtn);

                var date = new Date(Number(data.content[i].date));
                var compare = (new Date().getTime() - data.content[i].date) / 1000;
                var stringDate;
                if (compare <= 60)
                    stringDate = '방금전';
                else if (compare <= 3600)
                    stringDate = Math.floor(compare / 60) + "분전";
                else if (compare <= 86400)
                    stringDate = Math.floor(compare / 3600) + "시간전";
                else
                    stringDate = date.getFullYear() + "." + (date.getMonth() + 1) + "." + date.getDate();
                dateTd.innerHTML = stringDate;

                contentTr.appendChild(imgTd);
                contentTr.appendChild(nameTd);
                contentTr.appendChild(contentTd);
                contentTr.appendChild(evalTd);
                contentTr.appendChild(dateTd);

                if (sessionId == data.content[i].user.userId) {
                    var deleteBtn = document.createElement('button');
                    var deleteA = document.createElement('a');
                    deleteBtn.setAttribute('type', 'button');
                    deleteBtn.appendChild(deleteA);
                    deleteA.innerHTML = '삭제';
                    deleteA.setAttribute('href', '/delete/' + contentId);
                    evalTd.appendChild(deleteBtn);
                }

            }
            console.log(data);
            console.log(data.content[0].id);
            console.log(data.content[0].content);
            console.log(data.content[0].date);
            console.log(data.content[0].likeCount);
            console.log(data.content[0].dislikeCount);
            console.log(data.content[0].user.name);
            contents.innerHTML = "/<tr>"
        }
        else
            alert("Error loading page\n");
    }
};
req.send(null);
