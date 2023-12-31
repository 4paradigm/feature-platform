<template>
<div>

  <a-drawer
    v-model:visible="isOpenOfflineSampleDrawer"
    size="large"
    :title="$t('Offline Sample') + $t('Detail')">
    <OfflineSampleDetail :id="currentDrawerOfflineSample" :key="currentDrawerOfflineSample"></OfflineSampleDetail>
  </a-drawer>

  <!-- Data table -->
  <a-input v-model:value="searchText" :placeholder="$t('Search')" @change="handleSearch" />
  <br/><br/>

  <a-table :columns="columns" :data-source="searchFilteredOfflineSamples">
    <template #jobId="{ text, record }">
      <a-button type="link" @click="openOfflineSampleDrawer(record.jobId)">{{ record.jobId }}</a-button>
    </template>
    <template v-slot:custom="scope">
      <a-popconfirm
          title="Sure to delete?"
          @confirm="handleDelete(scope.record.jobId)">
        <a>{{ $t('Delete') }}</a>
      </a-popconfirm>
    </template>
  </a-table>

</div>
</template>
  
<script>
import axios from 'axios'
import { notification } from 'ant-design-vue'
import OfflineSampleDetail from '@/components/offlinesample/OfflineSampleDetail.vue'

export default {
  components: {
    OfflineSampleDetail
  },

  data() {
    return {
      isOpenOfflineSampleDrawer: false,
      currentDrawerOfflineSample: -1,
      
      searchText: "",
      searchFilteredOfflineSamples: [],
      offlineSamples: [],

      columns: [{
        title: this.$t('Offline Sample ID'),
        dataIndex: 'jobId',
        key: 'jobId',
        slots: { customRender: 'jobId' }
      },
      {
        title: this.$t('Features'),
        dataIndex: 'featureNames',
        key: 'featureName',
      },
      {
        title: this.$t('Path'),
        dataIndex: 'path',
        key: 'path',
      },
      {
        title: this.$t('Options'),
        dataIndex: 'options',
        key: 'options',
      },
      {
        title: this.$t('Actions'),
        key: 'actions',
        slots: { customRender: 'custom' },
      }],

    };
  },

  mounted() {
    this.initData();
  },

  methods: {
    initData() {
      axios.get("/api/offlinesamples")
        .then(response => {
          this.offlineSamples = response.data;
          this.searchFilteredOfflineSamples = this.offlineSamples;
        })
        .catch(error => {
          var errorMessage = error.message;
          if (error.response && error.response.data) {
            errorMessage = error.response.data;
          }
          notification["error"]({
            message: this.$t('Execute Fail'),
            description: errorMessage
          });
        });
    },

    matchSearch(item) {
        return item.id.toString().toLowerCase().includes(this.searchText.toLowerCase())
          || item.state.toLowerCase().includes(this.searchText.toLowerCase())
          || item.startTime.toLowerCase().includes(this.searchText.toLowerCase())
          || item.endTime.toLowerCase().includes(this.searchText.toLowerCase())
          || item.parameter.toLowerCase().includes(this.searchText.toLowerCase())
          || item.cluster.toLowerCase().includes(this.searchText.toLowerCase())
          || item.applicationId.toLowerCase().includes(this.searchText.toLowerCase())
          || item.error.toLowerCase().includes(this.searchText.toLowerCase());
    },

    handleSearch() {
      if (this.searchText === "") {
        this.searchFilteredOfflineSamples = this.offlineSamples;
      } else {
        this.searchFilteredOfflineSamples = this.offlineSamples.filter((item) => this.matchSearch(item));
      }
    },

    openOfflineSampleDrawer(id) {
      this.isOpenOfflineSampleDrawer = true;
      this.currentDrawerOfflineSample = id;
    },

    handleDelete(id) {
      axios.delete(`/api/offlinesamples/${id}`)
      .then(response => {
        notification["success"]({
              message: this.$t('Execute Success'),
              description: `Success to delete offline sample: ${id}`
            });

        this.initData();
      })
      .catch(error => {
        var errorMessage = error.message;
        if (error.response && error.response.data) {
          errorMessage = error.response.data;
        }
        notification["error"]({
          message: this.$t('Execute Fail'),
          description: errorMessage
        });
      });
    },

  }
};
</script>