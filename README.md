# screenplay-web
An example of screenplay pattern used for web automation

For writing har files, look at https://github.com/blibli-badak/selenium-har-util?trk=article-ssr-frontend-pulse_little-text-block

# grid setup example

## 1 Install [Kind](https://kind.sigs.k8s.io/docs/user/quick-start/#installing-from-release-binaries)
```
mkdir kind
```

## 2 Configure k8s cluster
```
cat kind/test-cluster.yaml
```

```
kind: Cluster
apiVersion: kind.x-k8s.io/v1alpha4
name: test-kind
nodes:
- role: control-plane
  kubeadmConfigPatches:
    - |
      kind: InitConfiguration
      nodeRegistration:
      kubeletExtraArgs:
          node-labels: "ingress-ready=true"
  
  extraPortMappings:
    - containerPort: 80
      hostPort: 80
      protocol: TCP
    - containerPort: 443
      hostPort: 443
      protocol: TCP
```

## 3 Configure nginx ingress controller
```
cat kind/test-ingress-controller.yaml

# https://raw.githubusercontent.com/kubernetes/ingress-nginx/main/deploy/static/provider/kind/deploy.yaml
```

## 4 Create k8s cluster
```
kind create cluster --config kind/test-cluster.yaml
kubectl cluster-info --context kind-test-kind

kubectl config get-contexts
kubectl config use-context kind-test-kind
kubectl apply -f kind/test-ingress-controller.yaml
kubectl wait --namespace ingress-nginx --for=condition=ready pod --selector=app.kubernetes.io/component=controller --timeout=90s
```

## 5 Load images to k8s cluster
```
docker pull jaegertracing/all-in-one:1.53.0
kind load docker-image --name test-kind jaegertracing/all-in-one:1.53.0

docker pull selenium/node-chromium:131.0
kind load docker-image --name test-kind selenium/node-chromium:131.0
```

## 6 Add helm charts repositories
```
helm repo add docker-selenium https://www.selenium.dev/docker-selenium
helm repo update
helm search repo docker-selenium —versions
```

## 7 Configure grid
```
cat grid-values.yaml
```

```
isolateComponents: false

global:
httpLogs: true
logLevel: FINE

tracing:
enabled: true

autoscaling:
enabled: true
scaledOptions:
    minReplicaCount: 0
    maxReplicaCount: 8
    pollingInterval: 20

edgeNode:
enabled: false

firefoxNode:
enabled: false
```

* more grid values at https://github.com/SeleniumHQ/docker-selenium/blob/trunk/charts/selenium-grid/values.yaml

## 8 Deploy grid
```
helm install selenium-grid docker-selenium/selenium-grid --version 0.38.0 -f grid-values.yaml --set ingress.hostname=localhost --set chromeNode.imageTag=131.0 --set chromeNode.imageName=node-chromium

kubectl get deployments
kubectl get ingress
kubectl get all
```

* Access hub ui: http://localhost/
* Access jaeger ui: http://localhost/jaeger

## 9 Run tests
* Point webdriver to the grid `http://localhost:4444/wd/hub`
* Grab logs: `kind export logs $(pwd) --name test-kind`

## 10 Remove grid
```
helm list
helm uninstall selenium-grid
```

## 11 Delete k8s cluster
```
kind delete cluster --name test-kind
```