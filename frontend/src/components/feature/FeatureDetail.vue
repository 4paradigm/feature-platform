<template>
  <div>

    <h2>{{ $t('Feature') }}: {{ feature.featureName }} </h2>
    <a-descriptions bordered>
      <a-descriptions-item :span="24" :label="$t('Feature View Name')">{{ feature.featureViewName }}</a-descriptions-item>
      <a-descriptions-item :span="24" :label="$t('Feature Name')">{{ feature.featureName }}</a-descriptions-item>
      <a-descriptions-item :span="24" :label="$t('Type')">{{ feature.type}}</a-descriptions-item>
      <a-descriptions-item :span="24" :label="$t('Description')">{{ feature.description}}</a-descriptions-item>
    </a-descriptions>
  </div>
</template>
  
<script>
import axios from 'axios';
import { notification } from 'ant-design-vue'
import { onMounted, ref } from 'vue'

export default {
  props: {
    featureViewName: {
      type: String,
      required: true,
    },
    featureName: {
      type: String,
      required: true,
    },
  },

  data() {
    return {
      feature: {}
    };
  },

  methods: {
    initData() {
      axios.get(`/api/features/${this.featureViewName}/${this.featureName}`)
        .then(response => {
          this.feature = response.data;
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
    }
  },

  mounted() {
    this.initData();
  }
}
</script>
