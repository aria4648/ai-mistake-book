// ==================== 用户功能 ====================

// 切换到注册表单
function showRegister() {
    document.getElementById('loginForm').classList.add('hidden');
    document.getElementById('registerForm').classList.remove('hidden');
}

// 切换到登录表单
function showLogin() {
    document.getElementById('registerForm').classList.add('hidden');
    document.getElementById('loginForm').classList.remove('hidden');
}

// 注册
function register() {
    const data = {
        username: document.getElementById('regUsername').value,
        password: document.getElementById('regPassword').value,
        nickname: document.getElementById('regNickname').value
    };
    
    fetch('/api/user/register', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(data)
    })
    .then(res => res.json())
    .then(result => {
        if (result.code === 200) {
            alert('注册成功！请登录');
            showLogin();
        } else {
            alert(result.msg);
        }
    });
}

// 登录
function login() {
    const data = {
        username: document.getElementById('username').value,
        password: document.getElementById('password').value
    };
    
    fetch('/api/user/login', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(data)
    })
    .then(res => res.json())
    .then(result => {
        if (result.code === 200) {
            alert('登录成功！欢迎 ' + result.data.username);
            showMainPage(result.data);
        } else {
            alert(result.msg);
        }
    });
}

// 退出登录
function logout() {
    fetch('/api/user/logout', { method: 'POST' })
    .then(res => res.json())
    .then(() => {
        location.reload();
    });
}

// 显示主页面
function showMainPage(user) {
    document.getElementById('loginPage').classList.add('hidden');
    document.getElementById('mainPage').classList.remove('hidden');
    document.getElementById('welcomeMsg').textContent = '欢迎, ' + user.username;
    loadMistakes();
}

// ==================== 错题功能 ====================

// 加载错题列表
function loadMistakes() {
    const subject = document.getElementById('subjectFilter').value;
    let url = '/api/mistake';
    if (subject) {
        url += '?subject=' + encodeURIComponent(subject);
    }
    
    fetch(url)
    .then(res => res.json())
    .then(result => {
        const list = document.getElementById('mistakeList');
        if (result.code !== 200) {
            alert(result.msg);
            return;
        }
        
        const data = result.data;
        if (data.length === 0) {
            list.innerHTML = '<div class="empty">还没有错题，快去添加吧！</div>';
            return;
        }
        
        // 渲染列表
        let html = '';
        data.forEach(item => {
            const status = item.status === 1 ? '✅ 已复习' : '⏳ 未复习';
            // 如果已经有 AI 解析了，显示出来
            const analysisHtml = item.analysis ? `
                <div class="mistake-analysis">
                    <strong>🤖 AI 解析：</strong><br>
                    ${item.analysis.replace(/\n/g, '<br>')}
                </div>
            ` : '';
            html += `
                <div class="mistake-item">
                    <div class="mistake-title">📌 ${item.title || '（无题目标题）'}</div>
                    <div class="mistake-info">
                        <span>📚 ${item.subject || '未分类'}</span>
                        <span>💡 ${item.knowledgePoint || '-'}</span>
                        <span>${status}</span>
                    </div>
                    <div class="mistake-info">
                        ❌ 你的答案：${item.myAnswer || '-'}
                    </div>
                    <div class="mistake-info">
                        ✅ 正确答案：${item.correctAnswer || '-'}
                    </div>
                    ${analysisHtml}
                    <div class="mistake-actions">
                        <button class="btn-ai" onclick="analyzeMistake(${item.id})">🤖 AI 解析</button>
                        <button class="btn-delete" onclick="deleteMistake(${item.id})">删除</button>
                    </div>
                </div>
            `;
        });
        list.innerHTML = html;
    });
}

// 新增错题
function addMistake() {
    const data = {
        title: document.getElementById('title').value,
        myAnswer: document.getElementById('myAnswer').value,
        correctAnswer: document.getElementById('correctAnswer').value,
        subject: document.getElementById('subject').value,
        knowledgePoint: document.getElementById('knowledgePoint').value
    };
    
    fetch('/api/mistake', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(data)
    })
    .then(res => res.json())
    .then(result => {
        if (result.code === 200) {
            alert('添加成功！');
            document.getElementById('title').value = '';
            document.getElementById('myAnswer').value = '';
            document.getElementById('correctAnswer').value = '';
            document.getElementById('subject').value = '';
            document.getElementById('knowledgePoint').value = '';
            loadMistakes();
        } else {
            alert(result.msg);
        }
    });
}

// 删除错题
function deleteMistake(id) {
    if (!confirm('确定要删除这道错题吗？')) return;
    
    fetch('/api/mistake/' + id, { method: 'DELETE' })
    .then(res => res.json())
    .then(result => {
        if (result.code === 200) {
            alert('删除成功！');
            loadMistakes();
        } else {
            alert(result.msg);
        }
    });
}

// AI 生成解析 ⭐ 新功能！
function analyzeMistake(id) {
    if (!confirm('确定要让 AI 生成解析吗？（可能需要几秒）')) return;
    
    // 找到这个错题的 AI 按钮，改成"思考中..."并禁用
    const btn = event.target;
    btn.textContent = '🤖 思考中...';
    btn.disabled = true;
    btn.style.opacity = '0.6';
    
    fetch('/api/mistake/' + id + '/ai-analysis', { method: 'POST' })
    .then(res => res.json())
    .then(result => {
        if (result.code === 200) {
            loadMistakes();  // 刷新列表，显示 AI 解析
        } else {
            alert(result.msg);
            btn.textContent = '🤖 AI 解析';
            btn.disabled = false;
            btn.style.opacity = '1';
        }
    })
    .catch(err => {
        alert('请求失败：' + err.message);
        btn.textContent = '🤖 AI 解析';
        btn.disabled = false;
        btn.style.opacity = '1';
    });
}

// ==================== 页面加载时检查登录状态 ====================
fetch('/api/user/info')
.then(res => res.json())
.then(result => {
    if (result.code === 200) {
        showMainPage(result.data);
    }
});
