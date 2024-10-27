<template>
  <div class="container">
    <el-container>
      <el-header class="header">通讯录管理系统</el-header>
      <el-main>
        <!-- Add Contact Button -->
        <el-button type="primary" class="add-button" @click="openAddDialog">添加联系人</el-button>

        <!-- Search Box -->
        <el-input
          placeholder="搜索联系人..."
          v-model="searchQuery"
          prefix-icon="el-icon-search"
          class="search-input"
        ></el-input>

        <!-- Contacts Table -->
        <el-table
          :data="paginatedContacts"
          style="width: 100%"
          stripe
          header-cell-class-name="table-header"
        >
          <el-table-column prop="name" label="姓名" width="180"></el-table-column>
          <el-table-column prop="address" label="住址"></el-table-column>
          <el-table-column prop="phone" label="电话"></el-table-column>
          <el-table-column label="操作" width="220">
            <template slot-scope="scope">
              <el-button size="mini" @click="openEditDialog(scope.row)">编辑</el-button>
              <el-button size="mini" type="danger" @click="deleteContact(scope.row.id)">删除</el-button>
            </template>
          </el-table-column>
        </el-table>

        <!-- Pagination -->
        <el-pagination
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
          :current-page="currentPage"
          :page-sizes="[5, 10, 20, 50]"
          :page-size="pageSize"
          layout="total, sizes, prev, pager, next, jumper"
          :total="filteredContacts.length"
          class="pagination"
        ></el-pagination>

        <el-dialog :title="dialogTitle" :visible.sync="dialogVisible" width="30%">
          <el-form :model="contactForm" label-width="80px">
            <el-form-item label="姓名">
              <el-input v-model="contactForm.name" placeholder="请输入姓名"></el-input>
            </el-form-item>
            <el-form-item label="住址">
              <el-input v-model="contactForm.address" placeholder="请输入住址"></el-input>
            </el-form-item>
            <el-form-item label="电话">
              <el-input v-model="contactForm.phone" placeholder="请输入电话"></el-input>
            </el-form-item>
          </el-form>
          <span slot="footer" class="dialog-footer">
            <el-button @click="dialogVisible = false">取消</el-button>
            <el-button type="primary" @click="saveContact">确定</el-button>
          </span>
        </el-dialog>
      </el-main>
    </el-container>
  </div>
</template>

<script>
import axios from 'axios';

export default {
  data() {
    return {
      dialogTitle: '',
      dialogVisible: false,
      contactForm: {
        id: null, // Add id to contactForm
        name: '',
        address: '',
        phone: ''
      },
      contacts: [],
      currentPage: 1,
      pageSize: 5,
      searchQuery: ''
    };
  },
  computed: {
    filteredContacts() {
      return this.contacts.filter(contact => 
        contact.name.includes(this.searchQuery)
      );
    },
    paginatedContacts() {
      const start = (this.currentPage - 1) * this.pageSize;
      const end = start + this.pageSize;
      return this.filteredContacts.slice(start, end);
    }
  },
  methods: {
    openAddDialog() {
      this.dialogTitle = '添加联系人';
      this.dialogVisible = true;
      this.resetContactForm();
    },
    openEditDialog(contact) {
      this.dialogTitle = '编辑联系人';
      this.dialogVisible = true;
      this.contactForm = { ...contact };
    },
    async deleteContact(id) {
      try {
        await this.$confirm('此操作将永久删除该数据, 是否继续?', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        });
        const resp = await this.$http.delete("/api/contact/" + id);
        if (resp.data.code == 200) {
          this.$message.success("删除成功！");
          this.fetchContacts(); // Refresh contacts after deletion
        }
      } catch (error) {
        console.error('Error deleting contact:', error);
      }
    },
    saveContact() {
      const _self = this;
      try {
        if (this.contactForm.id) {
          // Update contact
          this.$http.put("/api/contact", this.contactForm).then(resp => {
            if (resp.data.code == 200) {
              this.$message.success(resp.data.msg);
              this.fetchContacts(); // Refresh contacts after update
            }
          });
        } else {
          // Add new contact
          this.$http.post("/api/contact", this.contactForm).then(resp => {
            if (resp.data.code == 200) {
              this.$message.success(resp.data.msg);
              this.fetchContacts(); // Refresh contacts after add
            }
          });
        }
        this.dialogVisible = false;
        this.resetContactForm();
      } catch (error) {
        console.error('Error saving contact:', error);
      }
    },
    resetContactForm() {
      this.contactForm.id = null;
      this.contactForm.name = '';
      this.contactForm.address = '';
      this.contactForm.phone = '';
    },
    handleSizeChange(newSize) {
      this.pageSize = newSize;
    },
    handleCurrentChange(newPage) {
      this.currentPage = newPage;
    },
    fetchContacts() {
      this.$http.get('/api/contact/')
        .then(response => {
          this.contacts = response.data.data.ContactInfos;
        })
        .catch(error => {
          console.error('错误：', error);
        });
    }
  },
  created() {
    this.fetchContacts();
  }
};
</script>

<style scoped>
.container {
  margin: 30px auto;
  max-width: 1200px;
  background-color: #fff;
  border-radius: 10px;
  padding: 20px;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
}
.header {
  background-color: #409EFF;
  color: white;
  text-align: center;
  line-height: 60px;
  font-size: 24px;
}
.add-button {
  margin-bottom: 20px;
}
.search-input {
  margin-bottom: 20px;
  width: 300px;
}
.table-header {
  background-color: #f5f5f5;
  color: #333;
}
.pagination {
  margin-top: 20px;
  text-align: right;
}
.dialog-footer {
  text-align: right;
}
</style>